package org.iobserve.analysis.model;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;

import de.uka.ipd.sdq.dsexplore.qml.declarations.QMLDeclarations.QMLDeclarations;
import de.uka.ipd.sdq.dsexplore.qml.declarations.QMLDeclarations.QMLDeclarationsPackage;

/**
 * TODO add description.
 *
 * @author unkown
 *
 */
public class QMLDeclarationsModelProvider extends AbstractModelProvider<QMLDeclarations> {

    public QMLDeclarationsModelProvider(final URI theUriModelInstance) {
        super(theUriModelInstance);
    }

    @Override
    protected EPackage getPackage() {
        return QMLDeclarationsPackage.eINSTANCE;
    }

    @Override
    public void resetModel() {
        this.getModel().getQmlDeclarations().clear();
    }

}
