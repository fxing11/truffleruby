/*
 * Copyright (c) 2013, 2019 Oracle and/or its affiliates. All rights reserved. This
 * code is released under a tri EPL/GPL/LGPL license. You can use it,
 * redistribute it and/or modify it under the terms of the:
 *
 * Eclipse Public License version 2.0, or
 * GNU General Public License version 2, or
 * GNU Lesser General Public License version 2.1.
 */
package org.truffleruby.language.control;

import org.truffleruby.core.cast.BooleanCastNode;
import org.truffleruby.core.cast.BooleanCastNodeGen;
import org.truffleruby.language.ContextSourceRubyNode;
import org.truffleruby.language.RubyNode;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.profiles.ConditionProfile;

public class IfElseNode extends ContextSourceRubyNode {

    @Child private BooleanCastNode condition;
    @Child private RubyNode thenBody;
    @Child private RubyNode elseBody;

    private final ConditionProfile conditionProfile = ConditionProfile.createCountingProfile();

    public IfElseNode(RubyNode condition, RubyNode thenBody, RubyNode elseBody) {
        this.condition = BooleanCastNodeGen.create(condition);
        this.thenBody = thenBody;
        this.elseBody = elseBody;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        if (conditionProfile.profile(condition.executeBoolean(frame))) {
            return thenBody.execute(frame);
        } else {
            return elseBody.execute(frame);
        }
    }

}
