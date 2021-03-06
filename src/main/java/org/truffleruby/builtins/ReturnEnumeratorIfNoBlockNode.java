/*
 * Copyright (c) 2013, 2019 Oracle and/or its affiliates. All rights reserved. This
 * code is released under a tri EPL/GPL/LGPL license. You can use it,
 * redistribute it and/or modify it under the terms of the:
 *
 * Eclipse Public License version 2.0, or
 * GNU General Public License version 2, or
 * GNU Lesser General Public License version 2.1.
 */
package org.truffleruby.builtins;

import org.truffleruby.core.array.ArrayUtils;
import org.truffleruby.language.RubyContextSourceNode;
import org.truffleruby.language.RubyNode;
import org.truffleruby.language.arguments.RubyArguments;
import org.truffleruby.language.dispatch.CallDispatchHeadNode;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.object.DynamicObject;
import com.oracle.truffle.api.profiles.ConditionProfile;

public class ReturnEnumeratorIfNoBlockNode extends RubyContextSourceNode {

    private final String methodName;
    @Child private RubyNode method;
    @Child private CallDispatchHeadNode toEnumNode;
    @CompilationFinal private DynamicObject methodSymbol;
    private final ConditionProfile noBlockProfile = ConditionProfile.create();

    public ReturnEnumeratorIfNoBlockNode(String methodName, RubyNode method) {
        this.methodName = methodName;
        this.method = method;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        final DynamicObject block = RubyArguments.getBlock(frame);

        if (noBlockProfile.profile(block == null)) {
            if (toEnumNode == null) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                toEnumNode = insert(CallDispatchHeadNode.createPrivate());
            }

            if (methodSymbol == null) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                methodSymbol = getSymbol(methodName);
            }

            final Object[] arguments = ArrayUtils.unshift(RubyArguments.getArguments(frame), methodSymbol);
            return toEnumNode.call(RubyArguments.getSelf(frame), "to_enum", arguments);
        } else {
            return method.execute(frame);
        }
    }

}
