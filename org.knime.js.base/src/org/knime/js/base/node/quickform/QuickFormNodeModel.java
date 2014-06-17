/*
 * ------------------------------------------------------------------------
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
 * ------------------------------------------------------------------------
 */
package org.knime.js.base.node.quickform;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettings;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.dialog.DialogNode;
import org.knime.core.node.dialog.DialogNodeValue;
import org.knime.core.node.port.PortType;
import org.knime.core.node.web.ValidationError;
import org.knime.core.node.web.WebViewContent;
import org.knime.core.node.wizard.WizardNode;

/**
 * @author Christian Albrecht, KNIME.com AG, Zurich, Switzerland
 * @param <REP> The configuration content of the quickform node.
 * @param <VAL> The node value implementation of the quickform node.
 *
 */
public abstract class QuickFormNodeModel<REP extends QuickFormRepresentationImpl<VAL>,
        VAL extends DialogNodeValue & WebViewContent, CONF extends QuickFormConfig>
        extends NodeModel implements DialogNode<REP, VAL>, WizardNode<REP, VAL> {

    private static final NodeLogger LOGGER = NodeLogger.getLogger(QuickFormNodeModel.class);

    private CONF m_config;

    private REP m_dialogRepresentation;
    private VAL m_dialogValue;
    private REP m_viewRepresentation;
    private VAL m_viewValue;

    private boolean m_isReexecute = false;

    /**
     * Creates a new quickform model with the given number (and types!) of input
     * and output types.
     *
     * @param inPortTypes an array of non-null in-port types
     * @param outPortTypes an array of non-null out-port types
     */
    protected QuickFormNodeModel(final PortType[] inPortTypes, final PortType[] outPortTypes, final CONF config) {
        super(inPortTypes, outPortTypes);
        m_viewRepresentation = createEmptyViewRepresentation();
        m_viewValue = createEmptyViewValue();
        m_dialogRepresentation = createEmptyViewRepresentation();
        m_dialogValue = createEmptyViewValue();
        m_config = config;
    }

    protected CONF getConfig() {
        return m_config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void loadInternals(final File nodeInternDir, final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        File repFile = new File(nodeInternDir, "viewrepresentation.xml");
        File valFile = new File(nodeInternDir, "viewvalue.xml");
        NodeSettingsRO repSettings = NodeSettings.loadFromXML(new FileInputStream(repFile));
        NodeSettingsRO valSettings = NodeSettings.loadFromXML(new FileInputStream(valFile));
        m_viewRepresentation = createEmptyViewRepresentation();
        m_viewValue = createEmptyViewValue();
        try {
            m_viewRepresentation.loadFromNodeSettings(repSettings);
            m_viewValue.loadFromNodeSettings(valSettings);
        } catch (InvalidSettingsException e) {
            // what to do?
            LOGGER.error("Error loading internals: ", e);
        }
        File drepFile = new File(nodeInternDir, "dialogrepresentation.xml");
        File dvalFile = new File(nodeInternDir, "dialogvalue.xml");
        NodeSettingsRO drepSettings = NodeSettings.loadFromXML(new FileInputStream(drepFile));
        NodeSettingsRO dvalSettings = NodeSettings.loadFromXML(new FileInputStream(dvalFile));
        m_dialogRepresentation = createEmptyViewRepresentation();
        m_dialogValue = createEmptyViewValue();
        try {
            m_dialogRepresentation.loadFromNodeSettings(drepSettings);
            m_dialogValue.loadFromNodeSettings(dvalSettings);
        } catch (InvalidSettingsException e) {
            // what to do?
            LOGGER.error("Error loading internals: ", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void saveInternals(final File nodeInternDir, final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        NodeSettings repSettings = new NodeSettings("viewrepresentation");
        NodeSettings valSettings = new NodeSettings("viewvalue");
        if (m_viewRepresentation != null) {
            m_viewRepresentation.saveToNodeSettings(repSettings);
        }
        if (m_viewValue != null) {
            m_viewValue.saveToNodeSettings(valSettings);
        }
        File repFile = new File(nodeInternDir, "viewrepresentation.xml");
        File valFile = new File(nodeInternDir, "viewvalue.xml");
        repSettings.saveToXML(new FileOutputStream(repFile));
        valSettings.saveToXML(new FileOutputStream(valFile));
        NodeSettings drepSettings = new NodeSettings("dialogrepresentation");
        NodeSettings dvalSettings = new NodeSettings("dialogvalue");
        if (m_dialogRepresentation != null) {
            m_dialogRepresentation.saveToNodeSettings(drepSettings);
        }
        if (m_dialogValue != null) {
            m_dialogValue.saveToNodeSettings(dvalSettings);
        }
        File drepFile = new File(nodeInternDir, "dialogrepresentation.xml");
        File dvalFile = new File(nodeInternDir, "dialogvalue.xml");
        drepSettings.saveToXML(new FileOutputStream(drepFile));
        dvalSettings.saveToXML(new FileOutputStream(dvalFile));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public REP getDialogRepresentation() {
        return m_dialogRepresentation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VAL getDialogValue() {
        return m_dialogValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public REP getViewRepresentation() {
        return m_viewRepresentation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VAL getViewValue() {
        return m_viewValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadViewValue(final VAL viewContent, final boolean useAsDefault) {
        m_viewValue = viewContent;
        if (useAsDefault) {
            copyValueToConfig();
        }
    };

    @Override
    protected void reset() {
        m_viewRepresentation = createEmptyViewRepresentation();
        m_viewValue = createEmptyViewValue();
        m_isReexecute = false;
    }

    protected void setExecuted() {
        m_isReexecute = true;
    }

    protected boolean isReexecute() {
        return m_isReexecute;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        m_config.loadSettings(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        m_config.saveSettings(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        //
    }

    @Override
    public ValidationError validateViewValue(final VAL viewContent) {
        return null;
    };

    protected void copyConfigToView() {
        getViewRepresentation().setLabel(getConfig().getLabel());
        getViewRepresentation().setDescription(getConfig().getDescription());
    }

    abstract protected void copyValueToConfig();

    protected void copyConfigToDialog() {
        getDialogRepresentation().setLabel(getConfig().getLabel());
        getDialogRepresentation().setDescription(getConfig().getDescription());
    }

}
