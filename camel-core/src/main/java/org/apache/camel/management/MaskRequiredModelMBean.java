/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.management;

import javax.management.Descriptor;
import javax.management.MBeanException;
import javax.management.MBeanOperationInfo;
import javax.management.ReflectionException;
import javax.management.RuntimeOperationsException;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.RequiredModelMBean;

import org.apache.camel.util.ObjectHelper;
import org.apache.camel.util.URISupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link RequiredModelMBean} which allows us to intercept invoking operations on the MBean.
 * <p/>
 * For example if mask has been enabled on JMX, then we use this implementation
 * to hide sensitive information from the returned JMX attributes / operations.
 */
@Deprecated
public class MaskRequiredModelMBean extends RequiredModelMBean {

    private static final Logger LOG = LoggerFactory.getLogger(MaskRequiredModelMBean.class);
    private boolean mask;

    public MaskRequiredModelMBean() throws MBeanException, RuntimeOperationsException {
        // must have default no-arg constructor
    }

    public MaskRequiredModelMBean(ModelMBeanInfo mbi, boolean mask) throws MBeanException, RuntimeOperationsException {
        super(mbi);
        this.mask = mask;
    }

    public boolean isMask() {
        return mask;
    }

    @Override
    public Object invoke(String opName, Object[] opArgs, String[] sig) throws MBeanException, ReflectionException {
        Object answer = super.invoke(opName, opArgs, sig);
        // mask the answer if enabled and it was a String type (we cannot mask other types)
        if (mask && answer instanceof String && ObjectHelper.isNotEmpty(answer) && isMaskOperation(opName)) {
            answer = mask(opName, (String) answer);
        }
        return answer;
    }

    protected boolean isMaskOperation(String opName) {
        for (MBeanOperationInfo info : getMBeanInfo().getOperations()) {
            if (info.getName().equals(opName)) {
                Descriptor desc = info.getDescriptor();
                if (desc != null) {
                    Object val = desc.getFieldValue("mask");
                    return val != null && "true".equals(val);
                }
            }
        }
        return false;
    }

    /**
     * Masks the returned value from invoking the operation
     *
     * @param opName  the operation name invoked
     * @param value   the current value
     * @return the masked value
     */
    protected String mask(String opName, String value) {
        // use sanitize uri which will mask sensitive information
        String answer = URISupport.sanitizeUri(value);
        if (LOG.isTraceEnabled()) {
            LOG.trace("Masking JMX operation: {}.{} value: {} -> {}",
                    new Object[]{getMBeanInfo().getClassName(), opName, value, answer});
        }
        return answer;
    }
}
