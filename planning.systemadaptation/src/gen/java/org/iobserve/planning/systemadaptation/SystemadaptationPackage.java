/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package org.iobserve.planning.systemadaptation;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc --> The <b>Package</b> for the model. It contains accessors for the meta
 * objects to represent
 * <ul>
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * 
 * @see org.iobserve.planning.systemadaptation.SystemadaptationFactory
 * @model kind="package"
 * @generated
 */
public interface SystemadaptationPackage extends EPackage {
    /**
     * The package name. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    String eNAME = "systemadaptation";

    /**
     * The package namespace URI. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    String eNS_URI = "http://iobserve.org/SystemAdaptation";

    /**
     * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    String eNS_PREFIX = "org.iobserve";

    /**
     * The singleton instance of the package. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    SystemadaptationPackage eINSTANCE = org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl.init();

    /**
     * The meta object id for the
     * '{@link org.iobserve.planning.systemadaptation.impl.SystemAdaptationImpl <em>System
     * Adaptation</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.iobserve.planning.systemadaptation.impl.SystemAdaptationImpl
     * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getSystemAdaptation()
     * @generated
     */
    int SYSTEM_ADAPTATION = 0;

    /**
     * The feature id for the '<em><b>Actions</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int SYSTEM_ADAPTATION__ACTIONS = 0;

    /**
     * The number of structural features of the '<em>System Adaptation</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int SYSTEM_ADAPTATION_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link org.iobserve.planning.systemadaptation.impl.ActionImpl
     * <em>Action</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.iobserve.planning.systemadaptation.impl.ActionImpl
     * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getAction()
     * @generated
     */
    int ACTION = 1;

    /**
     * The feature id for the '<em><b>Resource Container</b></em>' reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int ACTION__RESOURCE_CONTAINER = 0;

    /**
     * The number of structural features of the '<em>Action</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int ACTION_FEATURE_COUNT = 1;

    /**
     * The meta object id for the
     * '{@link org.iobserve.planning.systemadaptation.impl.AssemblyContextActionImpl <em>Assembly
     * Context Action</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.iobserve.planning.systemadaptation.impl.AssemblyContextActionImpl
     * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getAssemblyContextAction()
     * @generated
     */
    int ASSEMBLY_CONTEXT_ACTION = 2;

    /**
     * The feature id for the '<em><b>Resource Container</b></em>' reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int ASSEMBLY_CONTEXT_ACTION__RESOURCE_CONTAINER = SystemadaptationPackage.ACTION__RESOURCE_CONTAINER;

    /**
     * The feature id for the '<em><b>Source Assembly Context</b></em>' reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int ASSEMBLY_CONTEXT_ACTION__SOURCE_ASSEMBLY_CONTEXT = SystemadaptationPackage.ACTION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Assembly Context Action</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT = SystemadaptationPackage.ACTION_FEATURE_COUNT + 1;

    /**
     * The meta object id for the
     * '{@link org.iobserve.planning.systemadaptation.impl.ResourceContainerActionImpl <em>Resource
     * Container Action</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.iobserve.planning.systemadaptation.impl.ResourceContainerActionImpl
     * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getResourceContainerAction()
     * @generated
     */
    int RESOURCE_CONTAINER_ACTION = 3;

    /**
     * The feature id for the '<em><b>Resource Container</b></em>' reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int RESOURCE_CONTAINER_ACTION__RESOURCE_CONTAINER = SystemadaptationPackage.ACTION__RESOURCE_CONTAINER;

    /**
     * The feature id for the '<em><b>Source Resource Container</b></em>' reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int RESOURCE_CONTAINER_ACTION__SOURCE_RESOURCE_CONTAINER = SystemadaptationPackage.ACTION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Resource Container Action</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int RESOURCE_CONTAINER_ACTION_FEATURE_COUNT = SystemadaptationPackage.ACTION_FEATURE_COUNT + 1;

    /**
     * The meta object id for the
     * '{@link org.iobserve.planning.systemadaptation.impl.ChangeRepositoryComponentActionImpl
     * <em>Change Repository Component Action</em>}' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see org.iobserve.planning.systemadaptation.impl.ChangeRepositoryComponentActionImpl
     * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getChangeRepositoryComponentAction()
     * @generated
     */
    int CHANGE_REPOSITORY_COMPONENT_ACTION = 4;

    /**
     * The feature id for the '<em><b>Resource Container</b></em>' reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int CHANGE_REPOSITORY_COMPONENT_ACTION__RESOURCE_CONTAINER = SystemadaptationPackage.ASSEMBLY_CONTEXT_ACTION__RESOURCE_CONTAINER;

    /**
     * The feature id for the '<em><b>Source Assembly Context</b></em>' reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int CHANGE_REPOSITORY_COMPONENT_ACTION__SOURCE_ASSEMBLY_CONTEXT = SystemadaptationPackage.ASSEMBLY_CONTEXT_ACTION__SOURCE_ASSEMBLY_CONTEXT;

    /**
     * The feature id for the '<em><b>New Repository Component</b></em>' reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int CHANGE_REPOSITORY_COMPONENT_ACTION__NEW_REPOSITORY_COMPONENT = SystemadaptationPackage.ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT
            + 0;

    /**
     * The number of structural features of the '<em>Change Repository Component Action</em>' class.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int CHANGE_REPOSITORY_COMPONENT_ACTION_FEATURE_COUNT = SystemadaptationPackage.ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT
            + 1;

    /**
     * The meta object id for the
     * '{@link org.iobserve.planning.systemadaptation.impl.ReplicateActionImpl <em>Replicate
     * Action</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.iobserve.planning.systemadaptation.impl.ReplicateActionImpl
     * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getReplicateAction()
     * @generated
     */
    int REPLICATE_ACTION = 5;

    /**
     * The feature id for the '<em><b>Resource Container</b></em>' reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int REPLICATE_ACTION__RESOURCE_CONTAINER = SystemadaptationPackage.ASSEMBLY_CONTEXT_ACTION__RESOURCE_CONTAINER;

    /**
     * The feature id for the '<em><b>Source Assembly Context</b></em>' reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int REPLICATE_ACTION__SOURCE_ASSEMBLY_CONTEXT = SystemadaptationPackage.ASSEMBLY_CONTEXT_ACTION__SOURCE_ASSEMBLY_CONTEXT;

    /**
     * The feature id for the '<em><b>New Allocation Context</b></em>' reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int REPLICATE_ACTION__NEW_ALLOCATION_CONTEXT = SystemadaptationPackage.ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Replicate Action</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int REPLICATE_ACTION_FEATURE_COUNT = SystemadaptationPackage.ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT + 1;

    /**
     * The meta object id for the
     * '{@link org.iobserve.planning.systemadaptation.impl.DereplicateActionImpl <em>Dereplicate
     * Action</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.iobserve.planning.systemadaptation.impl.DereplicateActionImpl
     * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getDereplicateAction()
     * @generated
     */
    int DEREPLICATE_ACTION = 6;

    /**
     * The feature id for the '<em><b>Resource Container</b></em>' reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int DEREPLICATE_ACTION__RESOURCE_CONTAINER = SystemadaptationPackage.ASSEMBLY_CONTEXT_ACTION__RESOURCE_CONTAINER;

    /**
     * The feature id for the '<em><b>Source Assembly Context</b></em>' reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int DEREPLICATE_ACTION__SOURCE_ASSEMBLY_CONTEXT = SystemadaptationPackage.ASSEMBLY_CONTEXT_ACTION__SOURCE_ASSEMBLY_CONTEXT;

    /**
     * The feature id for the '<em><b>Old Allocation Context</b></em>' reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int DEREPLICATE_ACTION__OLD_ALLOCATION_CONTEXT = SystemadaptationPackage.ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Dereplicate Action</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int DEREPLICATE_ACTION_FEATURE_COUNT = SystemadaptationPackage.ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT + 1;

    /**
     * The meta object id for the
     * '{@link org.iobserve.planning.systemadaptation.impl.MigrateActionImpl <em>Migrate
     * Action</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.iobserve.planning.systemadaptation.impl.MigrateActionImpl
     * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getMigrateAction()
     * @generated
     */
    int MIGRATE_ACTION = 7;

    /**
     * The feature id for the '<em><b>Resource Container</b></em>' reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int MIGRATE_ACTION__RESOURCE_CONTAINER = SystemadaptationPackage.ASSEMBLY_CONTEXT_ACTION__RESOURCE_CONTAINER;

    /**
     * The feature id for the '<em><b>Source Assembly Context</b></em>' reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int MIGRATE_ACTION__SOURCE_ASSEMBLY_CONTEXT = SystemadaptationPackage.ASSEMBLY_CONTEXT_ACTION__SOURCE_ASSEMBLY_CONTEXT;

    /**
     * The feature id for the '<em><b>New Allocation Context</b></em>' reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int MIGRATE_ACTION__NEW_ALLOCATION_CONTEXT = SystemadaptationPackage.ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Source Allocation Context</b></em>' reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int MIGRATE_ACTION__SOURCE_ALLOCATION_CONTEXT = SystemadaptationPackage.ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Migrate Action</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int MIGRATE_ACTION_FEATURE_COUNT = SystemadaptationPackage.ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT + 2;

    /**
     * The meta object id for the
     * '{@link org.iobserve.planning.systemadaptation.impl.AllocateActionImpl <em>Allocate
     * Action</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.iobserve.planning.systemadaptation.impl.AllocateActionImpl
     * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getAllocateAction()
     * @generated
     */
    int ALLOCATE_ACTION = 8;

    /**
     * The feature id for the '<em><b>Resource Container</b></em>' reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int ALLOCATE_ACTION__RESOURCE_CONTAINER = SystemadaptationPackage.RESOURCE_CONTAINER_ACTION__RESOURCE_CONTAINER;

    /**
     * The feature id for the '<em><b>Source Resource Container</b></em>' reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int ALLOCATE_ACTION__SOURCE_RESOURCE_CONTAINER = SystemadaptationPackage.RESOURCE_CONTAINER_ACTION__SOURCE_RESOURCE_CONTAINER;

    /**
     * The number of structural features of the '<em>Allocate Action</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int ALLOCATE_ACTION_FEATURE_COUNT = SystemadaptationPackage.RESOURCE_CONTAINER_ACTION_FEATURE_COUNT + 0;

    /**
     * The meta object id for the
     * '{@link org.iobserve.planning.systemadaptation.impl.DeallocateActionImpl <em>Deallocate
     * Action</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.iobserve.planning.systemadaptation.impl.DeallocateActionImpl
     * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getDeallocateAction()
     * @generated
     */
    int DEALLOCATE_ACTION = 9;

    /**
     * The feature id for the '<em><b>Resource Container</b></em>' reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int DEALLOCATE_ACTION__RESOURCE_CONTAINER = SystemadaptationPackage.RESOURCE_CONTAINER_ACTION__RESOURCE_CONTAINER;

    /**
     * The feature id for the '<em><b>Source Resource Container</b></em>' reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int DEALLOCATE_ACTION__SOURCE_RESOURCE_CONTAINER = SystemadaptationPackage.RESOURCE_CONTAINER_ACTION__SOURCE_RESOURCE_CONTAINER;

    /**
     * The number of structural features of the '<em>Deallocate Action</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    int DEALLOCATE_ACTION_FEATURE_COUNT = SystemadaptationPackage.RESOURCE_CONTAINER_ACTION_FEATURE_COUNT + 0;

    /**
     * Returns the meta object for class
     * '{@link org.iobserve.planning.systemadaptation.SystemAdaptation <em>System Adaptation</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>System Adaptation</em>'.
     * @see org.iobserve.planning.systemadaptation.SystemAdaptation
     * @generated
     */
    EClass getSystemAdaptation();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.iobserve.planning.systemadaptation.SystemAdaptation#getActions
     * <em>Actions</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the containment reference list '<em>Actions</em>'.
     * @see org.iobserve.planning.systemadaptation.SystemAdaptation#getActions()
     * @see #getSystemAdaptation()
     * @generated
     */
    EReference getSystemAdaptation_Actions();

    /**
     * Returns the meta object for class '{@link org.iobserve.planning.systemadaptation.Action
     * <em>Action</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Action</em>'.
     * @see org.iobserve.planning.systemadaptation.Action
     * @generated
     */
    EClass getAction();

    /**
     * Returns the meta object for the reference
     * '{@link org.iobserve.planning.systemadaptation.Action#getResourceContainer <em>Resource
     * Container</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the reference '<em>Resource Container</em>'.
     * @see org.iobserve.planning.systemadaptation.Action#getResourceContainer()
     * @see #getAction()
     * @generated
     */
    EReference getAction_ResourceContainer();

    /**
     * Returns the meta object for class
     * '{@link org.iobserve.planning.systemadaptation.AssemblyContextAction <em>Assembly Context
     * Action</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Assembly Context Action</em>'.
     * @see org.iobserve.planning.systemadaptation.AssemblyContextAction
     * @generated
     */
    EClass getAssemblyContextAction();

    /**
     * Returns the meta object for the reference
     * '{@link org.iobserve.planning.systemadaptation.AssemblyContextAction#getSourceAssemblyContext
     * <em>Source Assembly Context</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the reference '<em>Source Assembly Context</em>'.
     * @see org.iobserve.planning.systemadaptation.AssemblyContextAction#getSourceAssemblyContext()
     * @see #getAssemblyContextAction()
     * @generated
     */
    EReference getAssemblyContextAction_SourceAssemblyContext();

    /**
     * Returns the meta object for class
     * '{@link org.iobserve.planning.systemadaptation.ResourceContainerAction <em>Resource Container
     * Action</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Resource Container Action</em>'.
     * @see org.iobserve.planning.systemadaptation.ResourceContainerAction
     * @generated
     */
    EClass getResourceContainerAction();

    /**
     * Returns the meta object for the reference
     * '{@link org.iobserve.planning.systemadaptation.ResourceContainerAction#getSourceResourceContainer
     * <em>Source Resource Container</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the reference '<em>Source Resource Container</em>'.
     * @see org.iobserve.planning.systemadaptation.ResourceContainerAction#getSourceResourceContainer()
     * @see #getResourceContainerAction()
     * @generated
     */
    EReference getResourceContainerAction_SourceResourceContainer();

    /**
     * Returns the meta object for class
     * '{@link org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction <em>Change
     * Repository Component Action</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Change Repository Component Action</em>'.
     * @see org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction
     * @generated
     */
    EClass getChangeRepositoryComponentAction();

    /**
     * Returns the meta object for the reference
     * '{@link org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction#getNewRepositoryComponent
     * <em>New Repository Component</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the reference '<em>New Repository Component</em>'.
     * @see org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction#getNewRepositoryComponent()
     * @see #getChangeRepositoryComponentAction()
     * @generated
     */
    EReference getChangeRepositoryComponentAction_NewRepositoryComponent();

    /**
     * Returns the meta object for class
     * '{@link org.iobserve.planning.systemadaptation.ReplicateAction <em>Replicate Action</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Replicate Action</em>'.
     * @see org.iobserve.planning.systemadaptation.ReplicateAction
     * @generated
     */
    EClass getReplicateAction();

    /**
     * Returns the meta object for the reference
     * '{@link org.iobserve.planning.systemadaptation.ReplicateAction#getNewAllocationContext
     * <em>New Allocation Context</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the reference '<em>New Allocation Context</em>'.
     * @see org.iobserve.planning.systemadaptation.ReplicateAction#getNewAllocationContext()
     * @see #getReplicateAction()
     * @generated
     */
    EReference getReplicateAction_NewAllocationContext();

    /**
     * Returns the meta object for class
     * '{@link org.iobserve.planning.systemadaptation.DereplicateAction <em>Dereplicate
     * Action</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Dereplicate Action</em>'.
     * @see org.iobserve.planning.systemadaptation.DereplicateAction
     * @generated
     */
    EClass getDereplicateAction();

    /**
     * Returns the meta object for the reference
     * '{@link org.iobserve.planning.systemadaptation.DereplicateAction#getOldAllocationContext
     * <em>Old Allocation Context</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the reference '<em>Old Allocation Context</em>'.
     * @see org.iobserve.planning.systemadaptation.DereplicateAction#getOldAllocationContext()
     * @see #getDereplicateAction()
     * @generated
     */
    EReference getDereplicateAction_OldAllocationContext();

    /**
     * Returns the meta object for class
     * '{@link org.iobserve.planning.systemadaptation.MigrateAction <em>Migrate Action</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Migrate Action</em>'.
     * @see org.iobserve.planning.systemadaptation.MigrateAction
     * @generated
     */
    EClass getMigrateAction();

    /**
     * Returns the meta object for the reference
     * '{@link org.iobserve.planning.systemadaptation.MigrateAction#getNewAllocationContext <em>New
     * Allocation Context</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the reference '<em>New Allocation Context</em>'.
     * @see org.iobserve.planning.systemadaptation.MigrateAction#getNewAllocationContext()
     * @see #getMigrateAction()
     * @generated
     */
    EReference getMigrateAction_NewAllocationContext();

    /**
     * Returns the meta object for the reference
     * '{@link org.iobserve.planning.systemadaptation.MigrateAction#getSourceAllocationContext
     * <em>Source Allocation Context</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for the reference '<em>Source Allocation Context</em>'.
     * @see org.iobserve.planning.systemadaptation.MigrateAction#getSourceAllocationContext()
     * @see #getMigrateAction()
     * @generated
     */
    EReference getMigrateAction_SourceAllocationContext();

    /**
     * Returns the meta object for class
     * '{@link org.iobserve.planning.systemadaptation.AllocateAction <em>Allocate Action</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Allocate Action</em>'.
     * @see org.iobserve.planning.systemadaptation.AllocateAction
     * @generated
     */
    EClass getAllocateAction();

    /**
     * Returns the meta object for class
     * '{@link org.iobserve.planning.systemadaptation.DeallocateAction <em>Deallocate Action</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the meta object for class '<em>Deallocate Action</em>'.
     * @see org.iobserve.planning.systemadaptation.DeallocateAction
     * @generated
     */
    EClass getDeallocateAction();

    /**
     * Returns the factory that creates the instances of the model. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the factory that creates the instances of the model.
     * @generated
     */
    SystemadaptationFactory getSystemadaptationFactory();

    /**
     * <!-- begin-user-doc --> Defines literals for the meta objects that represent
     * <ul>
     * <li>each class,</li>
     * <li>each feature of each class,</li>
     * <li>each enum,</li>
     * <li>and each data type</li>
     * </ul>
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    interface Literals {
        /**
         * The meta object literal for the
         * '{@link org.iobserve.planning.systemadaptation.impl.SystemAdaptationImpl <em>System
         * Adaptation</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @see org.iobserve.planning.systemadaptation.impl.SystemAdaptationImpl
         * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getSystemAdaptation()
         * @generated
         */
        EClass SYSTEM_ADAPTATION = SystemadaptationPackage.eINSTANCE.getSystemAdaptation();

        /**
         * The meta object literal for the '<em><b>Actions</b></em>' containment reference list
         * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference SYSTEM_ADAPTATION__ACTIONS = SystemadaptationPackage.eINSTANCE.getSystemAdaptation_Actions();

        /**
         * The meta object literal for the
         * '{@link org.iobserve.planning.systemadaptation.impl.ActionImpl <em>Action</em>}' class.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @see org.iobserve.planning.systemadaptation.impl.ActionImpl
         * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getAction()
         * @generated
         */
        EClass ACTION = SystemadaptationPackage.eINSTANCE.getAction();

        /**
         * The meta object literal for the '<em><b>Resource Container</b></em>' reference feature.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference ACTION__RESOURCE_CONTAINER = SystemadaptationPackage.eINSTANCE.getAction_ResourceContainer();

        /**
         * The meta object literal for the
         * '{@link org.iobserve.planning.systemadaptation.impl.AssemblyContextActionImpl
         * <em>Assembly Context Action</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @see org.iobserve.planning.systemadaptation.impl.AssemblyContextActionImpl
         * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getAssemblyContextAction()
         * @generated
         */
        EClass ASSEMBLY_CONTEXT_ACTION = SystemadaptationPackage.eINSTANCE.getAssemblyContextAction();

        /**
         * The meta object literal for the '<em><b>Source Assembly Context</b></em>' reference
         * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference ASSEMBLY_CONTEXT_ACTION__SOURCE_ASSEMBLY_CONTEXT = SystemadaptationPackage.eINSTANCE
                .getAssemblyContextAction_SourceAssemblyContext();

        /**
         * The meta object literal for the
         * '{@link org.iobserve.planning.systemadaptation.impl.ResourceContainerActionImpl
         * <em>Resource Container Action</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @see org.iobserve.planning.systemadaptation.impl.ResourceContainerActionImpl
         * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getResourceContainerAction()
         * @generated
         */
        EClass RESOURCE_CONTAINER_ACTION = SystemadaptationPackage.eINSTANCE.getResourceContainerAction();

        /**
         * The meta object literal for the '<em><b>Source Resource Container</b></em>' reference
         * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference RESOURCE_CONTAINER_ACTION__SOURCE_RESOURCE_CONTAINER = SystemadaptationPackage.eINSTANCE
                .getResourceContainerAction_SourceResourceContainer();

        /**
         * The meta object literal for the
         * '{@link org.iobserve.planning.systemadaptation.impl.ChangeRepositoryComponentActionImpl
         * <em>Change Repository Component Action</em>}' class. <!-- begin-user-doc --> <!--
         * end-user-doc -->
         * 
         * @see org.iobserve.planning.systemadaptation.impl.ChangeRepositoryComponentActionImpl
         * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getChangeRepositoryComponentAction()
         * @generated
         */
        EClass CHANGE_REPOSITORY_COMPONENT_ACTION = SystemadaptationPackage.eINSTANCE
                .getChangeRepositoryComponentAction();

        /**
         * The meta object literal for the '<em><b>New Repository Component</b></em>' reference
         * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference CHANGE_REPOSITORY_COMPONENT_ACTION__NEW_REPOSITORY_COMPONENT = SystemadaptationPackage.eINSTANCE
                .getChangeRepositoryComponentAction_NewRepositoryComponent();

        /**
         * The meta object literal for the
         * '{@link org.iobserve.planning.systemadaptation.impl.ReplicateActionImpl <em>Replicate
         * Action</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @see org.iobserve.planning.systemadaptation.impl.ReplicateActionImpl
         * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getReplicateAction()
         * @generated
         */
        EClass REPLICATE_ACTION = SystemadaptationPackage.eINSTANCE.getReplicateAction();

        /**
         * The meta object literal for the '<em><b>New Allocation Context</b></em>' reference
         * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference REPLICATE_ACTION__NEW_ALLOCATION_CONTEXT = SystemadaptationPackage.eINSTANCE
                .getReplicateAction_NewAllocationContext();

        /**
         * The meta object literal for the
         * '{@link org.iobserve.planning.systemadaptation.impl.DereplicateActionImpl <em>Dereplicate
         * Action</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @see org.iobserve.planning.systemadaptation.impl.DereplicateActionImpl
         * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getDereplicateAction()
         * @generated
         */
        EClass DEREPLICATE_ACTION = SystemadaptationPackage.eINSTANCE.getDereplicateAction();

        /**
         * The meta object literal for the '<em><b>Old Allocation Context</b></em>' reference
         * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference DEREPLICATE_ACTION__OLD_ALLOCATION_CONTEXT = SystemadaptationPackage.eINSTANCE
                .getDereplicateAction_OldAllocationContext();

        /**
         * The meta object literal for the
         * '{@link org.iobserve.planning.systemadaptation.impl.MigrateActionImpl <em>Migrate
         * Action</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @see org.iobserve.planning.systemadaptation.impl.MigrateActionImpl
         * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getMigrateAction()
         * @generated
         */
        EClass MIGRATE_ACTION = SystemadaptationPackage.eINSTANCE.getMigrateAction();

        /**
         * The meta object literal for the '<em><b>New Allocation Context</b></em>' reference
         * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference MIGRATE_ACTION__NEW_ALLOCATION_CONTEXT = SystemadaptationPackage.eINSTANCE
                .getMigrateAction_NewAllocationContext();

        /**
         * The meta object literal for the '<em><b>Source Allocation Context</b></em>' reference
         * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @generated
         */
        EReference MIGRATE_ACTION__SOURCE_ALLOCATION_CONTEXT = SystemadaptationPackage.eINSTANCE
                .getMigrateAction_SourceAllocationContext();

        /**
         * The meta object literal for the
         * '{@link org.iobserve.planning.systemadaptation.impl.AllocateActionImpl <em>Allocate
         * Action</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @see org.iobserve.planning.systemadaptation.impl.AllocateActionImpl
         * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getAllocateAction()
         * @generated
         */
        EClass ALLOCATE_ACTION = SystemadaptationPackage.eINSTANCE.getAllocateAction();

        /**
         * The meta object literal for the
         * '{@link org.iobserve.planning.systemadaptation.impl.DeallocateActionImpl <em>Deallocate
         * Action</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
         * 
         * @see org.iobserve.planning.systemadaptation.impl.DeallocateActionImpl
         * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getDeallocateAction()
         * @generated
         */
        EClass DEALLOCATE_ACTION = SystemadaptationPackage.eINSTANCE.getDeallocateAction();

    }

} // SystemadaptationPackage