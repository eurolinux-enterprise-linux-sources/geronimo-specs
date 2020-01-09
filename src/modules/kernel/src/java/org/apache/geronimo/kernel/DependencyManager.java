/**
 *
 * Copyright 2003-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.geronimo.kernel;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.MBeanServerNotification;
import javax.management.Notification;
import javax.management.NotificationFilterSupport;
import javax.management.NotificationListener;
import javax.management.ObjectName;

import org.apache.geronimo.kernel.jmx.JMXUtil;

/**
 * DependencyManager is the record keeper of the dependencies in Geronimo.  The DependencyManager
 * does not enforce any dependencies, it is simply a place where components can register their intent
 * to be dependent on another component.  Since a JMX Component can pretty much do whatever it wants
 * a component must watch the components it depends on to assure that they are following the
 * J2EE-Management state machine.
 * <p/>
 * The DependencyManager uses the nomenclature of parent-child where a child is dependent on a parent.
 * The names parent and child have no other meaning are just a convience to make the code readable.
 *
 * @version $Revision: 1.1 $ $Date: 2004/06/05 20:33:40 $
 * @jmx:mbean
 */
public class DependencyManager implements NotificationListener {
    /**
     * The mbean server we are registered with.
     */
    private MBeanServer mbeanServer;

    /**
     * A map from child names to a list of parents.
     */
    private final Map childToParentMap = new HashMap();

    /**
     * A map from parent back to a list of its children.
     */
    private final Map parentToChildMap = new HashMap();

    /**
     * A map from a component's ObjectName to the list of ObjectPatterns that the component is blocking
     * from starting.
     */
    private final Map startHoldsMap = new HashMap();

    public DependencyManager(MBeanServer mbeanServer) throws Exception {
        assert mbeanServer != null;
        this.mbeanServer = mbeanServer;
        NotificationFilterSupport mbeanServerFilter = new NotificationFilterSupport();
        mbeanServerFilter.enableType(MBeanServerNotification.UNREGISTRATION_NOTIFICATION);
        mbeanServer.addNotificationListener(JMXUtil.DELEGATE_NAME, this, mbeanServerFilter, null);
    }

    public synchronized void close() {
        try {
            mbeanServer.removeNotificationListener(JMXUtil.DELEGATE_NAME, this);
        } catch (JMException ignored) {
            // no big deal... just good citizen clean up code
        }
        mbeanServer = null;
        childToParentMap.clear();
        parentToChildMap.clear();
        startHoldsMap.clear();
    }

    /**
     * Declares a dependency from a child to a parent.
     *
     * @param child the dependent component
     * @param parent the component the child is depending on
     * @jmx:managed-operation
     */
    public synchronized void addDependency(ObjectName child, ObjectName parent) {
        Set parents = (Set) childToParentMap.get(child);
        if (parents == null) {
            parents = new HashSet();
            childToParentMap.put(child, parents);
        }
        parents.add(parent);

        Set children = (Set) parentToChildMap.get(parent);
        if (children == null) {
            children = new HashSet();
            parentToChildMap.put(parent, children);
        }
        children.add(child);
    }

    /**
     * Removes a dependency from a child to a parent
     *
     * @param child the dependnet component
     * @param parent the component that the child wil no longer depend on
     * @jmx:managed-operation
     */
    public synchronized void removeDependency(ObjectName child, ObjectName parent) {
        Set parents = (Set) childToParentMap.get(child);
        if (parents != null) {
            parents.remove(parent);
        }

        Set children = (Set) parentToChildMap.get(parent);
        if (children != null) {
            children.remove(child);
        }
    }

    /**
     * Removes all dependencies for a child
     *
     * @param child the component that will no longer depend on anything
     * @jmx:managed-operation
     */
    public synchronized void removeAllDependencies(ObjectName child) {
        Set parents = (Set) childToParentMap.remove(child);
        if (parents == null) {
            return;
        }
        for (Iterator iterator = parents.iterator(); iterator.hasNext();) {
            ObjectName parent = (ObjectName) iterator.next();
            Set children = (Set) parentToChildMap.get(parent);
            if (children != null) {
                children.remove(child);
            }

        }
    }

    /**
     * Adds dependencies from the child to every parent in the parents set
     *
     * @param child the dependent component
     * @param parents the set of components the child is depending on
     * @jmx:managed-operation
     */
    public synchronized void addDependencies(ObjectName child, Set parents) {
        Set existingParents = (Set) childToParentMap.get(child);
        if (existingParents == null) {
            existingParents = new HashSet(parents);
            childToParentMap.put(child, existingParents);
        } else {
            existingParents.addAll(parents);
        }

        for (Iterator i = parents.iterator(); i.hasNext();) {
            Object startParent = i.next();
            Set children = (Set) parentToChildMap.get(startParent);
            if (children == null) {
                children = new HashSet();
                parentToChildMap.put(startParent, children);
            }
            children.add(child);
        }
    }

    /**
     * Gets the set of parents that the child is depending on
     *
     * @param child the dependent component
     * @return a collection containing all of the components the child depends on; will never be null
     * @jmx:managed-operation
     */
    public synchronized Set getParents(ObjectName child) {
        Set parents = (Set) childToParentMap.get(child);
        if (parents == null) {
            return Collections.EMPTY_SET;
        }
        return parents;
    }

    /**
     * Gets all of the MBeans that have a dependency on the specified startParent.
     *
     * @param parent the component the returned childen set depend on
     * @return a collection containing all of the components that depend on the parent; will never be null
     * @jmx:managed-operation
     */
    public synchronized Set getChildren(ObjectName parent) {
        Set children = (Set) parentToChildMap.get(parent);
        if (children == null) {
            return Collections.EMPTY_SET;
        }
        return children;
    }

    /**
     * Adds a hold on a collection of object name patterns.  If the name of a component matches an object name
     * pattern in the collection, the component should not start.
     *
     * @param objectName the name of the component placing the holds
     * @param holds a collection of object name patterns which should not start
     * @jmx:managed-operation
     */
    public synchronized void addStartHolds(ObjectName objectName, Collection holds) {
        Collection currentHolds = (Collection) startHoldsMap.get(objectName);
        if (currentHolds == null) {
            currentHolds = new LinkedList(holds);
            startHoldsMap.put(objectName, currentHolds);
        } else {
            currentHolds.addAll(holds);
        }
    }

    /**
     * Removes a collection of holds.
     *
     * @param objectName the object name of the components owning the holds
     * @param holds a collection of the holds to remove
     * @jmx:managed-operation
     */
    public synchronized void removeStartHolds(ObjectName objectName, Collection holds) {
        Collection currentHolds = (Collection) startHoldsMap.get(objectName);
        if (currentHolds != null) {
            currentHolds.removeAll(holds);
        }
    }

    /**
     * Removes all of the holds owned by a component.
     *
     * @param objectName the object name of the component that will no longer have any holds
     * @jmx:managed-operation
     */
    public synchronized void removeAllStartHolds(ObjectName objectName) {
        startHoldsMap.remove(objectName);
    }

    /**
     * Gets the object name of the mbean blocking the start specified mbean.
     *
     * @param objectName the mbean to check for blockers
     * @return the mbean blocking the specified mbean, or null if there are no blockers
     * @jmx:managed-operation
     */
    public synchronized ObjectName checkBlocker(ObjectName objectName) {
        // check if objectName name is on one of the hold lists
        for (Iterator iterator = startHoldsMap.keySet().iterator(); iterator.hasNext();) {
            ObjectName blocker = (ObjectName) iterator.next();
            List holds = (List) startHoldsMap.get(blocker);
            for (Iterator holdsIterator = holds.iterator(); holdsIterator.hasNext();) {
                ObjectName pattern = (ObjectName) holdsIterator.next();
                if (pattern.apply(objectName)) {
                    return blocker;
                }
            }
        }
        return null;
    }

    public void handleNotification(Notification n, Object handback) {
        String type = n.getType();
        if (MBeanServerNotification.UNREGISTRATION_NOTIFICATION.equals(type)) {
            MBeanServerNotification notification = (MBeanServerNotification) n;
            ObjectName source = notification.getMBeanName();
            synchronized (this) {
                removeAllDependencies(source);
                removeAllStartHolds(source);
            }
        }
    }
}
