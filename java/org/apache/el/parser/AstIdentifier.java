/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/* Generated By:JJTree: Do not edit this line. AstIdentifier.java */

package org.apache.el.parser;

import javax.el.ELException;
import javax.el.MethodExpression;
import javax.el.MethodInfo;
import javax.el.MethodNotFoundException;
import javax.el.PropertyNotFoundException;
import javax.el.ValueExpression;
import javax.el.VariableMapper;

import org.apache.el.lang.EvaluationContext;
import org.apache.el.util.MessageFactory;
import org.apache.el.util.Validation;


/**
 * @author Jacob Hookom [jacob@hookom.net]
 * @version $Id$
 */
public final class AstIdentifier extends SimpleNode {
    public AstIdentifier(int id) {
        super(id);
    }

    @Override
    public Class<?> getType(EvaluationContext ctx) throws ELException {
        VariableMapper varMapper = ctx.getVariableMapper();
        if (varMapper != null) {
            ValueExpression expr = varMapper.resolveVariable(this.image);
            if (expr != null) {
                return expr.getType(ctx.getELContext());
            }
        }
        ctx.setPropertyResolved(false);
        Class<?> result = ctx.getELResolver().getType(ctx, null, this.image);
        if (!ctx.isPropertyResolved()) {
            throw new PropertyNotFoundException(MessageFactory.get(
                    "error.resolver.unhandled.null", this.image));
        }
        return result;
    }

    @Override
    public Object getValue(EvaluationContext ctx) throws ELException {
        VariableMapper varMapper = ctx.getVariableMapper();
        if (varMapper != null) {
            ValueExpression expr = varMapper.resolveVariable(this.image);
            if (expr != null) {
                return expr.getValue(ctx.getELContext());
            }
        }
        ctx.setPropertyResolved(false);
        Object result = ctx.getELResolver().getValue(ctx, null, this.image);
        if (!ctx.isPropertyResolved()) {
            throw new PropertyNotFoundException(MessageFactory.get(
                    "error.resolver.unhandled.null", this.image));
        }
        return result;
    }

    @Override
    public boolean isReadOnly(EvaluationContext ctx) throws ELException {
        VariableMapper varMapper = ctx.getVariableMapper();
        if (varMapper != null) {
            ValueExpression expr = varMapper.resolveVariable(this.image);
            if (expr != null) {
                return expr.isReadOnly(ctx.getELContext());
            }
        }
        ctx.setPropertyResolved(false);
        boolean result = ctx.getELResolver().isReadOnly(ctx, null, this.image);
        if (!ctx.isPropertyResolved()) {
            throw new PropertyNotFoundException(MessageFactory.get(
                    "error.resolver.unhandled.null", this.image));
        }
        return result;
    }

    @Override
    public void setValue(EvaluationContext ctx, Object value)
            throws ELException {
        VariableMapper varMapper = ctx.getVariableMapper();
        if (varMapper != null) {
            ValueExpression expr = varMapper.resolveVariable(this.image);
            if (expr != null) {
                expr.setValue(ctx.getELContext(), value);
                return;
            }
        }
        ctx.setPropertyResolved(false);
        ctx.getELResolver().setValue(ctx, null, this.image, value);
        if (!ctx.isPropertyResolved()) {
            throw new PropertyNotFoundException(MessageFactory.get(
                    "error.resolver.unhandled.null", this.image));
        }
    }

    @Override
    public Object invoke(EvaluationContext ctx, Class<?>[] paramTypes,
            Object[] paramValues) throws ELException {
        return this.getMethodExpression(ctx).invoke(ctx.getELContext(), paramValues);
    }
    

    @Override
    public MethodInfo getMethodInfo(EvaluationContext ctx,
            Class<?>[] paramTypes) throws ELException {
        return this.getMethodExpression(ctx).getMethodInfo(ctx.getELContext());
    }

    @Override
    public void setImage(String image) {
        if (Validation.isJavaKeyword(image)) {
            throw new ELException("Can't use Java keyword as identifier");
        }
        this.image = image;
    }

    private final MethodExpression getMethodExpression(EvaluationContext ctx)
            throws ELException {
        Object obj = null;

        // case A: ValueExpression exists, getValue which must
        // be a MethodExpression
        VariableMapper varMapper = ctx.getVariableMapper();
        ValueExpression ve = null;
        if (varMapper != null) {
            ve = varMapper.resolveVariable(this.image);
            if (ve != null) {
                obj = ve.getValue(ctx);
            }
        }

        // case B: evaluate the identity against the ELResolver, again, must be
        // a MethodExpression to be able to invoke
        if (ve == null) {
            ctx.setPropertyResolved(false);
            obj = ctx.getELResolver().getValue(ctx, null, this.image);
        }

        // finally provide helpful hints
        if (obj instanceof MethodExpression) {
            return (MethodExpression) obj;
        } else if (obj == null) {
            throw new MethodNotFoundException("Identity '" + this.image
                    + "' was null and was unable to invoke");
        } else {
            throw new ELException(
                    "Identity '"
                            + this.image
                            + "' does not reference a MethodExpression instance, returned type: "
                            + obj.getClass().getName());
        }
    }
}
