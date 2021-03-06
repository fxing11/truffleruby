/*
 * Copyright (c) 2013, 2019 Oracle and/or its affiliates. All rights reserved. This
 * code is released under a tri EPL/GPL/LGPL license. You can use it,
 * redistribute it and/or modify it under the terms of the:
 *
 * Eclipse Public License version 2.0, or
 * GNU General Public License version 2, or
 * GNU Lesser General Public License version 2.1.
 */
package org.truffleruby.language.exceptions;

import org.truffleruby.RubyContext;
import org.truffleruby.collections.BoundaryIterable;
import org.truffleruby.core.array.ArrayOperations;
import org.truffleruby.core.cast.SplatCastNode;
import org.truffleruby.core.cast.SplatCastNodeGen;
import org.truffleruby.language.RubyNode;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.object.DynamicObject;

public class RescueSplatNode extends RescueNode {

    @Child private RubyNode splatCastNode;

    public RescueSplatNode(RubyContext context, RubyNode handlingClassesArray, RubyNode rescueBody) {
        super(rescueBody);
        this.splatCastNode = SplatCastNodeGen.create(
                context,
                SplatCastNode.NilBehavior.EMPTY_ARRAY,
                true,
                handlingClassesArray);
    }

    @Override
    public boolean canHandle(VirtualFrame frame, DynamicObject exception) {
        final DynamicObject handlingClasses = (DynamicObject) splatCastNode.execute(frame);

        // TODO (norswap, eregon, 02 Mar 2020)
        //  This should use a node to iterate or we should move the logic to Ruby.
        //  This is only for rescue *array which seems very rare.
        for (Object handlingClass : new BoundaryIterable<>(ArrayOperations.toIterable(handlingClasses))) {
            if (matches(frame, exception, handlingClass)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Object isDefined(VirtualFrame frame, RubyContext context) {
        return RubyNode.defaultIsDefined(context, this);
    }

}
