package org.knime.js.base.node.quickform.input.molecule;

import java.awt.GridBagConstraints;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.js.base.node.quickform.QuickFormNodeDialog;

/**
 * 
 * @author Christian Albrecht, KNIME.com AG, Zurich, Switzerland, University of
 *         Konstanz
 */
public class MoleculeStringInputQuickFormNodeDialog extends QuickFormNodeDialog {

    private final JComboBox<String> m_formatBox;

    private final JTextArea m_valueArea;

    /** Constructors, inits fields calls layout routines. */
    MoleculeStringInputQuickFormNodeDialog() {
        m_formatBox = new JComboBox<String>(MoleculeStringInputQuickFormRepresentation.DEFAULT_FORMATS);
        m_formatBox.setEditable(true);
        m_valueArea = new JTextArea(3, DEF_TEXTFIELD_WIDTH);
        createAndAddTab();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void fillPanel(final JPanel panelWithGBLayout, final GridBagConstraints gbc) {
        addPairToPanel("Format: ", m_formatBox, panelWithGBLayout, gbc);
        addPairToPanel("Molecule String: ", new JScrollPane(m_valueArea), panelWithGBLayout, gbc);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadSettingsFrom(final NodeSettingsRO settings, final PortObjectSpec[] specs)
            throws NotConfigurableException {
        MoleculeStringInputQuickFormRepresentation representation = new MoleculeStringInputQuickFormRepresentation();
        representation.loadFromNodeSettingsInDialog(settings);
        loadSettingsFrom(representation);
        m_formatBox.setSelectedItem(representation.getFormat());
        MoleculeStringInputQuickFormValue value = new MoleculeStringInputQuickFormValue();
        value.loadFromNodeSettingsInDialog(settings);
        m_valueArea.setText(value.getString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) throws InvalidSettingsException {
        MoleculeStringInputQuickFormRepresentation representation = new MoleculeStringInputQuickFormRepresentation();
        saveSettingsTo(representation);
        representation.setFormat(m_formatBox.getSelectedItem().toString());
        representation.saveToNodeSettings(settings);
        MoleculeStringInputQuickFormValue value = new MoleculeStringInputQuickFormValue();
        value.setString(m_valueArea.getText());
        value.saveToNodeSettings(settings);
    }

}