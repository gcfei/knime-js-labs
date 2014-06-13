/*
 * ------------------------------------------------------------------------
 *
 *  Copyright by KNIME GmbH, Konstanz, Germany
 *  Website: http://www.knime.org; Email: contact@knime.org
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License, Version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 *  Additional permission under GNU GPL version 3 section 7:
 *
 *  KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs.
 *  Hence, KNIME and ECLIPSE are both independent programs and are not
 *  derived from each other. Should, however, the interpretation of the
 *  GNU GPL Version 3 ("License") under any applicable laws result in
 *  KNIME and ECLIPSE being a combined program, KNIME GMBH herewith grants
 *  you the additional permission to use and propagate KNIME together with
 *  ECLIPSE with only the license terms in place for ECLIPSE applying to
 *  ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the
 *  license terms of ECLIPSE themselves allow for the respective use and
 *  propagation of ECLIPSE together with KNIME.
 *
 *  Additional permission relating to nodes for KNIME that extend the Node
 *  Extension (and in particular that are based on subclasses of NodeModel,
 *  NodeDialog, and NodeView) and that only interoperate with KNIME through
 *  standard APIs ("Nodes"):
 *  Nodes are deemed to be separate and independent programs and to not be
 *  covered works.  Notwithstanding anything to the contrary in the
 *  License, the License does not apply to Nodes, you are not required to
 *  license Nodes under the License, and you are granted a license to
 *  prepare and propagate Nodes, in each case even if such Nodes are
 *  propagated with or for interoperation with KNIME.  The owner of a Node
 *  may freely choose the license terms applicable to such Node, including
 *  when such Node is propagated with or for interoperation with KNIME.
 * ---------------------------------------------------------------------
 *
 * History
 *   Jun 12, 2014 (winter): created
 */
package org.knime.js.base.node.quickform.input.integer;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.js.base.node.quickform.QuickFormFlowVariableConfig;

/**
 *
 * @author winter
 */
public class IntInputQuickFormConfig extends QuickFormFlowVariableConfig {

    private static final String CFG_USE_MIN = "use_min";
    private static final boolean DEFAULT_USE_MIN = false;
    private boolean m_useMin = DEFAULT_USE_MIN;
    private static final String CFG_USE_MAX = "use_max";
    private static final boolean DEFAULT_USE_MAX = false;
    private boolean m_useMax = DEFAULT_USE_MAX;
    private static final String CFG_MIN = "min";
    private static final int DEFAULT_MIN = 0;
    private int m_min = DEFAULT_MIN;
    private static final String CFG_MAX = "max";
    private static final int DEFAULT_MAX = 100;
    private int m_max = DEFAULT_MAX;
    private static final String CFG_DEFAULT = "default";
    private static final int DEFAULT_INTEGER = 0;
    private int m_defaultValue = DEFAULT_INTEGER;
    private static final String CFG_INTEGER = "integer";
    private int m_integer = DEFAULT_INTEGER;

    boolean getUseMin() {
        return m_useMin;
    }

    void setUseMin(final boolean useMin) {
        m_useMin = useMin;
    }

    boolean getUseMax() {
        return m_useMax;
    }

    void setUseMax(final boolean useMax) {
        m_useMax = useMax;
    }

    int getMin() {
        return m_min;
    }

    void setMin(final int min) {
        m_min = min;
    }

    int getMax() {
        return m_max;
    }

    void setMax(final int max) {
        m_max = max;
    }

    int getDefaultValue() {
        return m_defaultValue;
    }

    void setDefaultValue(final int defaultValue) {
        m_defaultValue = defaultValue;
    }

    int getInteger() {
        return m_integer;
    }

    void setInteger(final int integer) {
        m_integer = integer;
    }

    @Override
    public void saveSettings(final NodeSettingsWO settings) {
        super.saveSettings(settings);
        settings.addBoolean(CFG_USE_MIN, m_useMin);
        settings.addBoolean(CFG_USE_MAX, m_useMax);
        settings.addInt(CFG_MIN, m_min);
        settings.addInt(CFG_MAX, m_max);
        settings.addInt(CFG_DEFAULT, m_defaultValue);
        settings.addInt(CFG_INTEGER, m_integer);
    }

    @Override
    public void loadSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        super.loadSettings(settings);
        m_useMin = settings.getBoolean(CFG_USE_MIN);
        m_useMax = settings.getBoolean(CFG_USE_MAX);
        m_min = settings.getInt(CFG_MIN);
        m_max = settings.getInt(CFG_MAX);
        m_defaultValue = settings.getInt(CFG_DEFAULT);
        m_integer = settings.getInt(CFG_INTEGER);
    }

    @Override
    public void loadSettingsInDialog(final NodeSettingsRO settings) {
        super.loadSettingsInDialog(settings);
        m_useMin = settings.getBoolean(CFG_USE_MIN, DEFAULT_USE_MIN);
        m_useMax = settings.getBoolean(CFG_USE_MAX, DEFAULT_USE_MAX);
        m_min = settings.getInt(CFG_MIN, DEFAULT_MIN);
        m_max = settings.getInt(CFG_MAX, DEFAULT_MAX);
        m_defaultValue = settings.getInt(CFG_DEFAULT, DEFAULT_INTEGER);
        m_integer = settings.getInt(CFG_INTEGER, DEFAULT_INTEGER);
    }

}