/**
 * Copyright (c) 2014,2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.smarthome.core.library.types;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.eclipse.smarthome.core.types.State;
import org.eclipse.smarthome.core.types.UnDefType;

/**
 * The PercentType extends the {@link DecimalType} by putting constraints for its value on top (0-100).
 *
 * @author Kai Kreuzer - Initial contribution and API
 *
 */
public class PercentType extends DecimalType {

    private static final long serialVersionUID = -9066279845951780879L;

    public static final PercentType ZERO = new PercentType(0);
    public static final PercentType HUNDRED = new PercentType(100);

    public PercentType() {
        this(0);
    }

    public PercentType(int value) {
        super(value);
        validateValue(this.value);
    }

    public PercentType(String value) {
        super(value);
        validateValue(this.value);
    }

    public PercentType(BigDecimal value) {
        super(value);
        validateValue(this.value);
    }

    private void validateValue(BigDecimal value) {
        if (BigDecimal.ZERO.compareTo(value) > 0 || BigDecimal.valueOf(100).compareTo(value) < 0) {
            throw new IllegalArgumentException("Value must be between 0 and 100");
        }
    }

    public static PercentType valueOf(String value) {
        return new PercentType(value);
    }

    @Override
    public State as(Class<? extends State> target) {
        if (target == OnOffType.class) {
            return equals(ZERO) ? OnOffType.OFF : OnOffType.ON;
        } else if (target == DecimalType.class) {
            return new DecimalType(toBigDecimal().divide(BigDecimal.valueOf(100), 8, RoundingMode.UP));
        } else if (target == UpDownType.class) {
            if (equals(ZERO)) {
                return UpDownType.UP;
            } else if (equals(HUNDRED)) {
                return UpDownType.DOWN;
            } else {
                return UnDefType.UNDEF;
            }
        } else if (target == OpenClosedType.class) {
            if (equals(ZERO)) {
                return OpenClosedType.CLOSED;
            } else if (equals(HUNDRED)) {
                return OpenClosedType.OPEN;
            } else {
                return UnDefType.UNDEF;
            }
        } else if (target == HSBType.class) {
            return new HSBType(DecimalType.ZERO, PercentType.ZERO, this);
        } else {
            return defaultConversion(target);
        }
    }

}
