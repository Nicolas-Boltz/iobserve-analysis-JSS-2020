/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 ***************************************************************************/

package org.iobserve.analysis.filter.models.cdoruserbehavior;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.math3.util.Pair;
import org.iobserve.analysis.data.EntryCallEvent;
import org.iobserve.analysis.data.ExtendedEntryCallEvent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.qos.logback.core.net.SyslogOutputStream;

/**
 * table representation of a behavior model
 *
 * @author Christoph Dornieden
 *
 */

public class EditableBehaviorModelTable extends AbstractBehaviorModelTable {

    // a map for adding and updating transitions
    private final Map<String, Pair<Integer, ArrayList<AggregatedCallInformation>>> signatures;

    // a list for getting transitions
    private final ArrayList<String> inverseSignatures;

    // transition matrix
    private final LinkedList<LinkedList<Integer>> transitions;

    // filter list of regex expressions
    private final ArrayList<String> filterList;
    private final boolean blacklistMode;

    // Aggregation Strategy
    IRepresentativeStrategy strategy;

    /**
     * simple constructor
     */
    public EditableBehaviorModelTable(final IRepresentativeStrategy strategy) {
        this.signatures = new HashMap<>();
        this.inverseSignatures = new ArrayList<>();
        this.transitions = new LinkedList<>();
        this.filterList = new ArrayList<>();
        this.blacklistMode = true;
        this.strategy = strategy;
    }

    /**
     * advanced constructor
     *
     * @param filterList
     *            list of regex expressions to filter the signatures
     * @param blacklistMode
     *            true if the filterList is a blacklist, else a whitelist
     */

    public EditableBehaviorModelTable(final IRepresentativeStrategy strategy, final Collection<String> filterList,
            final boolean blacklistMode) {
        this.signatures = new HashMap<>();
        this.inverseSignatures = new ArrayList<>();
        this.transitions = new LinkedList<>();
        this.filterList = new ArrayList<>(filterList);
        this.blacklistMode = blacklistMode;
        this.strategy = strategy;

    }

    /**
     *
     * @param from
     *            where the transition comes
     * @param to
     *            where the transition goes *
     */
    @Override
    public void addTransition(final EntryCallEvent from, final EntryCallEvent to) throws IllegalArgumentException {

        final String fromSignature = AbstractBehaviorModelTable.getSignatureFromEvent(from);
        final String toSignature = AbstractBehaviorModelTable.getSignatureFromEvent(to);

        if (!(this.isAllowedSignature(fromSignature) && this.isAllowedSignature(toSignature))) {
            throw new IllegalArgumentException("event signature not allowed");
        }

        final Integer fromIndex = this.getSignatureIndex(fromSignature);
        final Integer toIndex = this.getSignatureIndex(toSignature);

        final Integer transitionCount = this.transitions.get(fromIndex).get(toIndex);
        this.transitions.get(fromIndex).set(toIndex, transitionCount + 1);
    }

    /**
     *
     * @param signature
     *            operation signature
     * @return index of the signature in the transition table
     */
    private int getSignatureIndex(final String signature) {
        final Integer index = this.signatures.containsKey(signature) ? this.signatures.get(signature).getFirst()
                : this.addSignature(signature);
        return index;
    }

    @Override
    public void addInformation(final ExtendedEntryCallEvent event) {
        final ObjectMapper objectMapper = new ObjectMapper();
        final String eventSignature = AbstractBehaviorModelTable.getSignatureFromEvent(event);
        final ArrayList<CallInformation> newCallInformations;

        try {
            newCallInformations = objectMapper.readValue(event.getInformations(),
                    new TypeReference<ArrayList<CallInformation>>() {
                    });
            // adding if no transition added yet
            if (this.signatures.size() == 0) {
                this.addSignature(eventSignature);
            }
            final ArrayList<AggregatedCallInformation> aggCallInformations = this.signatures.get(eventSignature)
                    .getSecond();

            for (final CallInformation newCallInformation : newCallInformations) {

                // add new CallInfromation to the aggregation correctly
                final List<AggregatedCallInformation> matches = aggCallInformations.stream()
                        .filter(aggCallInformation -> aggCallInformation.belongsTo(newCallInformation))
                        .collect(Collectors.toList());              
               
                
                if (matches.isEmpty()) {
                    // add new Callinformation
                   final AggregatedCallInformation newAggregatedCallInformation = new AggregatedCallInformation(
                            this.strategy, newCallInformation);
                    aggCallInformations.add(newAggregatedCallInformation);

                } else if (matches.size() == 1) {
                    matches.get(0).addCallInformation(newCallInformation);
                } else {
                    // TODO should not happen
                    System.out.println(matches.size() + "  Callinformations matched");
                }
            }

        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * extends the signature matrix
     *
     * @param signature
     *            to be added
     * @return Index of new signature
     */
    private Integer addSignature(final String signature) {

        final Integer size = this.signatures.size();

        this.signatures.put(signature, new Pair<>(size, new ArrayList<>()));
        this.inverseSignatures.add(signature);

        // extend all columns
        this.transitions.forEach(t -> t.addLast(0));

        // create a new row in the matrix
        final Integer[] newRowArray = new Integer[1 + size];
        Arrays.fill(newRowArray, 0);
        final LinkedList<Integer> newRow = new LinkedList<>(Arrays.asList(newRowArray));

        this.transitions.addLast(newRow);

        return size;
    }

    /**
     *
     * @return fixed size BehaviorModelTable
     */
    public BehaviorModelTable toFixedSizeBehaviorModelTable() {

        // create fixed signatures
        final Map<String, Pair<Integer, AggregatedCallInformation[]>> fixedSignatures = new HashMap<>();

        for (final String signature : this.signatures.keySet()) {

            final List<AggregatedCallInformation> aggCallInfoList = this.signatures.get(signature).getSecond();
            final AggregatedCallInformation[] aggregatedCallInformations = aggCallInfoList
                    .toArray(new AggregatedCallInformation[aggCallInfoList.size()]);

            final Pair<Integer, AggregatedCallInformation[]> fixedPair = new Pair<>(
                    this.signatures.get(signature).getFirst(), aggregatedCallInformations);
            fixedSignatures.put(signature, fixedPair);
        }

        final String[] fixedInverseSignatures = this.inverseSignatures.stream().toArray(String[]::new);

        // create transitions table
        final Integer[][] fixedTransitions = this.transitions.stream().map(l -> l.stream().toArray(Integer[]::new))
                .toArray(Integer[][]::new);

        final BehaviorModelTable fixedBehaviorModelTable = new BehaviorModelTable(fixedSignatures,
                fixedInverseSignatures, fixedTransitions);

        return fixedBehaviorModelTable;
    }

    /**
     *
     * @return cleared fixed size BehaviorModelTable
     */
    public BehaviorModelTable toClearedFixedSizeBehaviorModelTable() {

        // create fixed signatures
        final Map<String, Pair<Integer, AggregatedCallInformation[]>> fixedSignatures = new HashMap<>();

        for (final String signature : this.signatures.keySet()) {

            final List<AggregatedCallInformation> aggCallInfoList = this.signatures.get(signature).getSecond();

            final AggregatedCallInformation[] aggregatedCallInformations = aggCallInfoList.stream()
                    .map(aggCallInformation -> aggCallInformation.getClearedCopy())
                    .toArray(AggregatedCallInformation[]::new);

            final Pair<Integer, AggregatedCallInformation[]> fixedPair = new Pair<>(
                    this.signatures.get(signature).getFirst(), aggregatedCallInformations);
            fixedSignatures.put(signature, fixedPair);
        }

        final String[] fixedInverseSignatures = this.inverseSignatures.stream().toArray(String[]::new);

        // create transitions table
        final Integer[][] fixedTransitions = new Integer[fixedSignatures.size()][fixedSignatures.size()];

        final BehaviorModelTable fixedBehaviorModelTable = new BehaviorModelTable(fixedSignatures,
                fixedInverseSignatures, fixedTransitions);

        return fixedBehaviorModelTable;
    }

    @Override
    public boolean isAllowedSignature(final String signature) {
        boolean isAllowed = true;

        for (final String filterRule : this.filterList) {

            final boolean isMatch = signature.matches(filterRule);
            isAllowed = isAllowed && this.blacklistMode ? !isMatch : isMatch;
        }
        return isAllowed;
    }

    @Override
    public String toString() {
        final ObjectMapper objectMapper = new ObjectMapper();

        String string = "";
        String transitionString = "";
        try {
            for (final String signature : this.inverseSignatures) {
                string += signature + "\n";
            }

            transitionString = objectMapper.writeValueAsString(this.transitions);

            transitionString = transitionString.replaceAll("\\[\\[", "").replaceAll("\\]\\]", "");
            final String[] transitionStrings = transitionString.split("\\],\\[");

            for (final String row : transitionStrings) {
                string += row + "\n";
            }

        } catch (final JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //
        // for (final LinkedList<Integer> transitionlist : this.transitions) {
        //
        // for (final Integer transitioncount : transitionlist) {
        // string += transitionlist.toString().replaceAll("\\[|\\]", "") + ",";
        // }
        //
        // string += "\n";
        // }
        //
        return string;
    }

}