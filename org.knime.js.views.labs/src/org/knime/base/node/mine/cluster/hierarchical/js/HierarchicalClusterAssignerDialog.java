/*
 * ------------------------------------------------------------------------
 *
 *  Copyright by KNIME AG, Zurich, Switzerland
 *  Website: http://www.knime.com; Email: contact@knime.com
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
 *  KNIME and ECLIPSE being a combined program, KNIME AG herewith grants
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
 *   Jul 23, 2018 (awalter): created
 */
package org.knime.base.node.mine.cluster.hierarchical.js;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.port.PortObjectSpec;

/**
 *
 * @author Alison Walter
 */
public class HierarchicalClusterAssignerDialog extends NodeDialogPane {

    private static final int TEXT_FIELD_SIZE = 20;

    private final HierarchicalClusterAssignerConfig m_config;

    private final JCheckBox m_showWarningsInViewCheckBox;
    private final JCheckBox m_generateImageCheckBox;
    private final JSpinner m_imageWidthSpinner;
    private final JSpinner m_imageHeightSpinner;

    private final JCheckBox m_resizeToWindowCheckBox;
    private final JTextField m_titleTextField;
    private final JTextField m_subtitleTextField;
    private final JCheckBox m_enableClusterLabelsCheckBox;
    private final JCheckBox m_useLogScaleCheckBox;
    private final JComboBox<HierarchicalClusterAssignerOrientation> m_orientationComboBox;

    private final JCheckBox m_enableViewEditCheckBox;
    private final JCheckBox m_enableTitleEditCheckBox;
    private final JCheckBox m_displayFullscreenButtonCheckBox;
    private final JCheckBox m_enableNumClusterEditCheckBox;
    private final JCheckBox m_enableLogScaleToggleCheckBox;
    private final JCheckBox m_enableChangeOrientationCheckBox;

    private final JCheckBox m_enableSelectionCheckBox;
    private final JCheckBox m_publishSelectionEventsCheckBox;
    private final JCheckBox m_subscribeSelectionEventsCheckBox;
    private final JCheckBox m_showClearSelectionButtonCheckBox;
    private final JTextField m_selectionColumnNameTextField;
    private final JCheckBox m_showSelectedOnlyCheckBox;
    private final JCheckBox m_showSelectedOnlyToggleCheckBox;

    private final JSpinner m_numClustersSpinner;
    private final JSpinner m_distanceThresholdSpinner;
    private final JRadioButton m_clusterCountModeRadioButton;
    private final JRadioButton m_distanceThresholdModeRadioButton;
    private final JTextField m_clusterColumnNameTextField;
    private final JCheckBox m_useNormalizedDistancesCheckBox;


    private final JCheckBox m_enableZoomAndPanningCheckBox;
    private final JCheckBox m_showZoomResetButtonCheckBox;

    private final JRadioButton m_enableClusterColorRadioButton;
    private final JRadioButton m_enableTableColorRadioButton;
    private final JRadioButton m_useColorPaletteSet1RadioButton;
    private final JRadioButton m_useColorPaletteSet2RadioButton;
    private final JRadioButton m_useColorPaletteSet3RadioButton;

    private final JCheckBox m_subscribeFilterEventsCheckBox;

    private JCheckBox m_showThresholdBarCheckBox;
    private JCheckBox m_enableThresholdModificationCheckBox;

    private final JTextField m_xAxisLabelTextField;
    private final JTextField m_yAxisLabelTextField;
    private final JCheckBox m_enableAxisLabelEditCheckBox;

    HierarchicalClusterAssignerDialog() {
        m_config = new HierarchicalClusterAssignerConfig();

        m_numClustersSpinner = new JSpinner(
            new SpinnerNumberModel(HierarchicalClusterAssignerConfig.DEFAULT_NUM_CLUSTERS, 1, Integer.MAX_VALUE, 1));
        // maximum is 1, because use normalized distances is enabled by default
        m_distanceThresholdSpinner = new JSpinner(
            new SpinnerNumberModel(HierarchicalClusterAssignerConfig.DEFAULT_NORMALIZED_THRESHOLD, 0, 1, 0.01));
        m_clusterCountModeRadioButton = new JRadioButton("Cluster count");
        m_distanceThresholdModeRadioButton = new JRadioButton("Distance threshold");
        m_clusterColumnNameTextField = new JTextField(TEXT_FIELD_SIZE);
        m_useNormalizedDistancesCheckBox = new JCheckBox("Use normalized distances for threshold");

        final ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                m_numClustersSpinner.setEnabled(m_clusterCountModeRadioButton.isSelected());
                m_distanceThresholdSpinner.setEnabled(m_distanceThresholdModeRadioButton.isSelected());
                m_useNormalizedDistancesCheckBox.setEnabled(m_distanceThresholdModeRadioButton.isSelected());
            }
        };
        m_clusterCountModeRadioButton.addActionListener(al);
        m_distanceThresholdModeRadioButton.addActionListener(al);
        m_useNormalizedDistancesCheckBox.addActionListener(al);
        m_useNormalizedDistancesCheckBox.addChangeListener(e -> enableNormalizedDistances());

        m_showWarningsInViewCheckBox = new JCheckBox("Show warnings in view");
        m_generateImageCheckBox = new JCheckBox("Create image at outport");
        m_imageWidthSpinner = new JSpinner(new SpinnerNumberModel(100, 100, Integer.MAX_VALUE, 1));
        m_imageHeightSpinner = new JSpinner(new SpinnerNumberModel(100, 100, Integer.MAX_VALUE, 1));
        m_displayFullscreenButtonCheckBox = new JCheckBox("Display fullscreen button");
        m_resizeToWindowCheckBox = new JCheckBox("Resize view to fill window");
        m_titleTextField = new JTextField(TEXT_FIELD_SIZE);
        m_subtitleTextField = new JTextField(TEXT_FIELD_SIZE);
        m_enableViewEditCheckBox = new JCheckBox("Enable view edit controls");
        m_enableTitleEditCheckBox = new JCheckBox("Enable title and subtitle editing");
        m_enableNumClusterEditCheckBox = new JCheckBox("Enable number of clusters specification");
        m_enableClusterLabelsCheckBox = new JCheckBox("Enable cluster labels");
        m_enableSelectionCheckBox = new JCheckBox("Enable selection");
        m_publishSelectionEventsCheckBox = new JCheckBox("Publish selection events");
        m_subscribeSelectionEventsCheckBox = new JCheckBox("Subscribe to selection events");
        m_showClearSelectionButtonCheckBox = new JCheckBox("Show clear selection button");
        m_selectionColumnNameTextField = new JTextField(TEXT_FIELD_SIZE);
        m_showSelectedOnlyCheckBox = new JCheckBox("Show selected clusters only");
        m_showSelectedOnlyToggleCheckBox = new JCheckBox("Enable 'show selected clusters only' option");
        m_enableZoomAndPanningCheckBox = new JCheckBox("Enable zooming and panning");
        m_showZoomResetButtonCheckBox = new JCheckBox("Show zoom reset button");
        m_enableLogScaleToggleCheckBox= new JCheckBox("Enable switching y-axis scale");
        m_useLogScaleCheckBox = new JCheckBox("Use log scale for y-axis");
        m_enableChangeOrientationCheckBox = new JCheckBox("Enable changing chart orientation");
        m_orientationComboBox = new JComboBox<>();
        m_enableZoomAndPanningCheckBox.addChangeListener(
            e -> m_showZoomResetButtonCheckBox.setEnabled(m_enableZoomAndPanningCheckBox.isSelected()));
        for (final HierarchicalClusterAssignerOrientation type : HierarchicalClusterAssignerOrientation.values()) {
            m_orientationComboBox.addItem(type);
        }
        m_subscribeFilterEventsCheckBox = new JCheckBox("Subscribe to filter events");

        m_enableViewEditCheckBox.addChangeListener(e -> enableEditView());
        m_enableSelectionCheckBox.addChangeListener(e -> enableSelections());

        m_enableClusterColorRadioButton = new JRadioButton("Use cluster colors");
        m_enableTableColorRadioButton = new JRadioButton("Use table colors");
        m_useColorPaletteSet1RadioButton = new JRadioButton("Set 1");
        m_useColorPaletteSet2RadioButton = new JRadioButton("Set 2");
        m_useColorPaletteSet3RadioButton = new JRadioButton("Set 3 (color blind safe)");

        m_enableClusterColorRadioButton.addChangeListener(e -> enableColors());

        m_showThresholdBarCheckBox = new JCheckBox("Show threshold bar");
        m_enableThresholdModificationCheckBox = new JCheckBox("Enable threshold modification");
        m_showThresholdBarCheckBox.addChangeListener(
            e -> m_enableThresholdModificationCheckBox.setEnabled(m_showThresholdBarCheckBox.isSelected()));

        m_xAxisLabelTextField = new JTextField(TEXT_FIELD_SIZE);
        m_yAxisLabelTextField = new JTextField(TEXT_FIELD_SIZE);
        m_enableAxisLabelEditCheckBox = new JCheckBox("Enable axis label editing");

        addTab("Options", optionsPanel());
        addTab("View Configuration", viewConfigPanel());
        addTab("Interactivity", interactivityPanel());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) throws InvalidSettingsException {
        m_config.setShowWarningsInView(m_showWarningsInViewCheckBox.isSelected());
        m_config.setGenerateImage(m_generateImageCheckBox.isSelected());
        m_config.setImageWidth((int) m_imageWidthSpinner.getValue());
        m_config.setImageHeight((int) m_imageHeightSpinner.getValue());

        m_config.setResizeToWindow(m_resizeToWindowCheckBox.isSelected());
        m_config.setTitle(m_titleTextField.getText());
        m_config.setSubtitle(m_subtitleTextField.getText());
        m_config.setEnableClusterLabels(m_enableClusterLabelsCheckBox.isSelected());
        m_config.setUseLogScale(m_useLogScaleCheckBox.isSelected());
        m_config.setOrientation((HierarchicalClusterAssignerOrientation) m_orientationComboBox.getSelectedItem());

        m_config.setEnableViewEdit(m_enableViewEditCheckBox.isSelected());
        m_config.setEnableTitleEdit(m_enableTitleEditCheckBox.isSelected());
        m_config.setDisplayFullscreenButton(m_displayFullscreenButtonCheckBox.isSelected());
        m_config.setEnableNumClusterEdit(m_enableNumClusterEditCheckBox.isSelected());
        m_config.setEnableLogScaleToggle(m_enableLogScaleToggleCheckBox.isSelected());
        m_config.setEnableChangeOrientation(m_enableChangeOrientationCheckBox.isSelected());

        m_config.setEnableSelection(m_enableSelectionCheckBox.isSelected());
        m_config.setPublishSelectionEvents(m_publishSelectionEventsCheckBox.isSelected());
        m_config.setSubscribeSelectionEvents(m_subscribeSelectionEventsCheckBox.isSelected());
        m_config.setShowClearSelectionButton(m_showClearSelectionButtonCheckBox.isSelected());
        m_config.setSelectionColumnName(m_selectionColumnNameTextField.getText());
        m_config.setShowSelectedOnly(m_showSelectedOnlyCheckBox.isSelected());
        m_config.setShowSelectedOnlyToggle(m_showSelectedOnlyToggleCheckBox.isSelected());

        m_config.setSubscribeFilterEvents(m_subscribeFilterEventsCheckBox.isSelected());

        m_config.setNumClusters((int) m_numClustersSpinner.getValue());
        m_config.setNumClustersMode(m_clusterCountModeRadioButton.isSelected());
        m_config.setClusterColumnName(m_clusterColumnNameTextField.getText());
        m_config.setUseNormalizedDistances(m_useNormalizedDistancesCheckBox.isSelected());
        if (m_useNormalizedDistancesCheckBox.isSelected()) {
            m_config.setNormalizedThreshold((double) m_distanceThresholdSpinner.getValue());
        }
        else {
            m_config.setThreshold((double) m_distanceThresholdSpinner.getValue());
        }

        m_config.setEnableZoomAndPanning(m_enableZoomAndPanningCheckBox.isSelected());
        m_config.setShowZoomResetButton(m_showZoomResetButtonCheckBox.isSelected());

        m_config.setEnableClusterColor(m_enableClusterColorRadioButton.isSelected());
        m_config.setColorPalette(getColorPalette());

        m_config.setShowThresholdBar(m_showThresholdBarCheckBox.isSelected());
        m_config.setEnableThresholdModification(m_enableThresholdModificationCheckBox.isSelected());

        m_config.setXAxisLabel(m_xAxisLabelTextField.getText());
        m_config.setYAxisLabel(m_yAxisLabelTextField.getText());
        m_config.setEnableAxisLabelEdit(m_enableAxisLabelEditCheckBox.isSelected());

        m_config.saveSettings(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadSettingsFrom(final NodeSettingsRO settings, final PortObjectSpec[] specs) throws NotConfigurableException {
        final DataTableSpec spec = (DataTableSpec)specs[0];
        m_config.loadSettingsForDialog(settings, spec);

        m_showWarningsInViewCheckBox.setSelected(m_config.getShowWarningsInView());
        m_generateImageCheckBox.setSelected(m_config.getGenerateImage());
        m_imageWidthSpinner.setValue(m_config.getImageWidth());
        m_imageHeightSpinner.setValue(m_config.getImageHeight());

        m_resizeToWindowCheckBox.setSelected(m_config.getResizeToWindow());
        m_titleTextField.setText(m_config.getTitle());
        m_subtitleTextField.setText(m_config.getSubtitle());
        m_enableClusterLabelsCheckBox.setSelected(m_config.getEnableClusterLabels());
        m_useLogScaleCheckBox.setSelected(m_config.getUseLogScale());
        m_orientationComboBox.setSelectedItem(m_config.getOrientation());

        m_enableViewEditCheckBox.setSelected(m_config.getEnableViewEdit());
        m_enableTitleEditCheckBox.setSelected(m_config.getEnableTitleEdit());
        m_displayFullscreenButtonCheckBox.setSelected(m_config.getDisplayFullscreenButton());
        m_enableNumClusterEditCheckBox.setSelected(m_config.getEnableNumClusterEdit());
        m_enableLogScaleToggleCheckBox.setSelected(m_config.getEnableLogScaleToggle());
        m_enableChangeOrientationCheckBox.setSelected(m_config.getEnableChangeOrientation());

        m_enableSelectionCheckBox.setSelected(m_config.getEnableSelection());
        final boolean showSelectedOnly = m_config.getShowSelectedOnly();
        m_publishSelectionEventsCheckBox.setSelected(m_config.getPublishSelectionEvents());
        m_subscribeSelectionEventsCheckBox.setSelected(m_config.getSubscribeSelectionEvents());
        m_showClearSelectionButtonCheckBox.setSelected(m_config.getShowClearSelectionButton());
        m_selectionColumnNameTextField.setText(m_config.getSelectionColumnName());
        m_showSelectedOnlyCheckBox.setSelected(showSelectedOnly);
        m_showSelectedOnlyToggleCheckBox.setEnabled(showSelectedOnly);
        m_showSelectedOnlyToggleCheckBox.setSelected(m_config.getShowSelectedOnlyToggle());

        m_subscribeFilterEventsCheckBox.setSelected(m_config.getSubscribeFilterEvents());

        m_clusterCountModeRadioButton.setSelected(m_config.getNumClustersMode());
        m_distanceThresholdModeRadioButton.setSelected(!m_config.getNumClustersMode());
        m_numClustersSpinner.setEnabled(m_clusterCountModeRadioButton.isSelected());
        m_distanceThresholdSpinner.setEnabled(m_distanceThresholdModeRadioButton.isSelected());
        m_clusterColumnNameTextField.setText(m_config.getClusterColumnName());
        m_useNormalizedDistancesCheckBox.setSelected(m_config.getUseNormalizedDistances());
        m_useNormalizedDistancesCheckBox.setEnabled(m_distanceThresholdModeRadioButton.isSelected());
        m_numClustersSpinner.setValue(m_config.getNumClusters());
        if (m_useNormalizedDistancesCheckBox.isSelected()) {
            m_distanceThresholdSpinner.setValue(m_config.getNormalizedThreshold());
        } else {
            m_distanceThresholdSpinner.setValue(m_config.getThreshold());
        }

        final boolean zoomAndPanningEnabled = m_config.getEnableZoomAndPanning();
        m_enableZoomAndPanningCheckBox.setSelected(zoomAndPanningEnabled);
        m_showZoomResetButtonCheckBox.setSelected(m_config.getShowZoomResetButton());
        m_showZoomResetButtonCheckBox.setEnabled(zoomAndPanningEnabled);

        m_enableClusterColorRadioButton.setSelected(m_config.getEnableClusterColor());
        m_enableTableColorRadioButton.setSelected(!m_config.getEnableClusterColor());
        setColorPaletteRadioButtons(m_config.getColorPalette());

        final boolean showThresholdBar = m_config.getShowThresholdBar();
        m_showThresholdBarCheckBox.setSelected(showThresholdBar);
        m_enableThresholdModificationCheckBox.setSelected(m_config.getEnableThresholdModification());
        m_enableThresholdModificationCheckBox.setEnabled(showThresholdBar);

        m_xAxisLabelTextField.setText(m_config.getXAxisLabel());
        m_yAxisLabelTextField.setText(m_config.getYAxisLabel());
        m_enableAxisLabelEditCheckBox.setSelected(m_config.getEnableAxisLabelEdit());

        setNumberOfFilters((DataTableSpec) specs[1]);
        enableEditView();
        enableSelections();
        enableColors();
        getPanel().repaint();
    }

    // -- Helper methods --

    private void setNumberOfFilters(final DataTableSpec spec) {
        int numFilters = 0;
        for (int i = 0; i < spec.getNumColumns(); i++) {
            if (spec.getColumnSpec(i).getFilterHandler().isPresent()) {
                numFilters++;
            }
        }
        StringBuilder builder = new StringBuilder("Subscribe to filter events");
        builder.append(" (");
        builder.append(numFilters == 0 ? "no" : numFilters);
        builder.append(numFilters == 1 ? " filter" : " filters");
        builder.append(" available)");
        m_subscribeFilterEventsCheckBox.setText(builder.toString());
    }

    private void enableEditView() {
        final boolean enabled = m_enableViewEditCheckBox.isSelected();
        m_enableTitleEditCheckBox.setEnabled(enabled);
        m_enableNumClusterEditCheckBox.setEnabled(enabled);
        m_displayFullscreenButtonCheckBox.setEnabled(enabled);
        m_enableLogScaleToggleCheckBox.setEnabled(enabled);
        m_enableChangeOrientationCheckBox.setEnabled(enabled);
        m_enableAxisLabelEditCheckBox.setEnabled(enabled);
    }

    private void enableSelections() {
        final boolean enabled = m_enableSelectionCheckBox.isSelected();
        m_publishSelectionEventsCheckBox.setEnabled(enabled);
        m_subscribeSelectionEventsCheckBox.setEnabled(enabled);
        m_showClearSelectionButtonCheckBox.setEnabled(enabled);
        m_selectionColumnNameTextField.setEnabled(enabled);
        m_showSelectedOnlyCheckBox.setEnabled(enabled);
        m_showSelectedOnlyToggleCheckBox.setEnabled(enabled);
    }

    private void enableNormalizedDistances() {
        if (m_useNormalizedDistancesCheckBox.isSelected()) {
            if ((double)m_distanceThresholdSpinner.getValue() > 1) {
                m_distanceThresholdSpinner.setValue(1);
            }
            ((SpinnerNumberModel)m_distanceThresholdSpinner.getModel()).setMaximum(new Double(1));
        } else {
            ((SpinnerNumberModel)m_distanceThresholdSpinner.getModel()).setMaximum(Double.MAX_VALUE);
        }
    }

    private void enableColors() {
        final boolean enabled = m_enableClusterColorRadioButton.isSelected();
        m_useColorPaletteSet1RadioButton.setEnabled(enabled);
        m_useColorPaletteSet2RadioButton.setEnabled(enabled);
        m_useColorPaletteSet3RadioButton.setEnabled(enabled);
    }

    private Component optionsPanel() {
        final JPanel p = new JPanel(new GridBagLayout());
        final ButtonGroup bg = new ButtonGroup();
        bg.add(m_clusterCountModeRadioButton);
        bg.add(m_distanceThresholdModeRadioButton);

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 2;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridy++;

        p.add(new JLabel("Cluster assignment column name: "), gbc);
        gbc.gridx += 2;
        p.add(m_clusterColumnNameTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;

        p.add(new JLabel("Assign clusters based on:"), gbc);
        gbc.gridx = 2;
        p.add(m_clusterCountModeRadioButton, gbc);
        gbc.gridy++;
        p.add(m_distanceThresholdModeRadioButton, gbc);

        gbc.gridx = 0;
        gbc.gridy++;

        p.add(new JLabel("Number of clusters"), gbc);
        gbc.gridx = 2;
        p.add(m_numClustersSpinner, gbc);
        ((JSpinner.NumberEditor)m_numClustersSpinner.getEditor()).getTextField().setColumns(5);

        gbc.gridx = 0;
        gbc.gridy++;

        p.add(new JLabel("Distance threshold"), gbc);
        gbc.gridx = 2;
        p.add(m_distanceThresholdSpinner, gbc);
        ((JSpinner.NumberEditor)m_distanceThresholdSpinner.getEditor()).getTextField().setColumns(5);
        gbc.gridy++;
        p.add(m_useNormalizedDistancesCheckBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;

        return p;
    }

    private Component viewConfigPanel() {
        final JPanel p = new JPanel(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridy++;

        // General
        final JPanel generalPanel = new JPanel(new GridBagLayout());
        generalPanel.setBorder(BorderFactory.createTitledBorder("General"));
        p.add(generalPanel, gbc);
        final GridBagConstraints generalConstraints = new GridBagConstraints();
        generalConstraints.insets = new Insets(5, 5, 5, 5);
        generalConstraints.anchor = GridBagConstraints.NORTHWEST;
        generalConstraints.gridx = 0;
        generalConstraints.gridy = 0;
        generalConstraints.weightx = 1;
        generalPanel.add(m_showWarningsInViewCheckBox, generalConstraints);
        generalConstraints.gridx++;
        generalPanel.add(m_generateImageCheckBox, generalConstraints);
        generalConstraints.gridx = 0;
        generalConstraints.gridy++;
        generalPanel.add(m_resizeToWindowCheckBox, generalConstraints);
        generalConstraints.gridx = 0;
        generalConstraints.gridy++;
        generalPanel.add(new JLabel("Width (px): "), generalConstraints);
        generalConstraints.gridx++;
        generalPanel.add(m_imageWidthSpinner, generalConstraints);
        generalConstraints.gridx = 0;
        generalConstraints.gridy++;
        generalPanel.add(new JLabel("Height (px): "), generalConstraints);
        generalConstraints.gridx++;
        generalPanel.add(m_imageHeightSpinner, generalConstraints);
        generalConstraints.gridx = 0;
        generalConstraints.gridy++;
        generalPanel.add(m_showThresholdBarCheckBox, generalConstraints);
        generalConstraints.gridx++;
        generalPanel.add(m_enableThresholdModificationCheckBox, generalConstraints);
        generalConstraints.gridx = 0;
        generalConstraints.gridy++;

        gbc.gridx = 0;
        gbc.gridy++;

        // Display
        final JPanel displayPanel = new JPanel(new GridBagLayout());
        displayPanel.setBorder(BorderFactory.createTitledBorder("Display"));
        p.add(displayPanel, gbc);
        final GridBagConstraints displayConstraints = new GridBagConstraints();
        displayConstraints.insets = new Insets(5, 5, 5, 5);
        displayConstraints.anchor = GridBagConstraints.NORTHWEST;
        displayConstraints.gridx = 0;
        displayConstraints.gridy = 0;
        displayConstraints.weightx = 1;
        displayPanel.add(new JLabel("Chart title: "), displayConstraints);
        displayConstraints.gridx++;
        displayPanel.add(m_titleTextField, displayConstraints);
        displayConstraints.gridx = 0;
        displayConstraints.gridy++;
        displayPanel.add(new JLabel("Chart subtitle: "), displayConstraints);
        displayConstraints.gridx++;
        displayPanel.add(m_subtitleTextField, displayConstraints);
        displayConstraints.gridx = 0;
        displayConstraints.gridy++;
        displayPanel.add(new JLabel("X-axis label: "), displayConstraints);
        displayConstraints.gridx++;
        displayPanel.add(m_xAxisLabelTextField, displayConstraints);
        displayConstraints.gridx = 0;
        displayConstraints.gridy++;
        displayPanel.add(new JLabel("Y-axis label: "), displayConstraints);
        displayConstraints.gridx++;
        displayPanel.add(m_yAxisLabelTextField, displayConstraints);
        displayConstraints.gridx = 0;
        displayConstraints.gridy++;
        /* These settings will not be implemented for the first version
         *
        displayPanel.add(new JLabel("Chart Orientation: "), displayConstraints);
        displayConstraints.gridx++;
        displayPanel.add(m_orientationComboBox, displayConstraints);
        displayConstraints.gridx = 0;
        displayConstraints.gridy++;
        displayPanel.add(m_enableClusterLabelsCheckBox, displayConstraints);
        displayConstraints.gridx++;
        */
        displayPanel.add(m_useLogScaleCheckBox, displayConstraints);
        displayConstraints.gridx = 0;
        displayConstraints.gridy++;

        gbc.gridx = 0;
        gbc.gridy++;

        // Color
        /* These settings will not be implemented in the first version
         *
        final JPanel colorPanel = new JPanel(new GridBagLayout());
        colorPanel.setBorder(BorderFactory.createTitledBorder("Color"));
        final Component set1 = ColorPaletteUtil.getColorPaletteSet1AsComponent();
        set1.setEnabled(false);
        p.add(colorPanel, gbc);
        final ButtonGroup clusterOrTableColors = new ButtonGroup();
        clusterOrTableColors.add(m_enableClusterColorRadioButton);
        clusterOrTableColors.add(m_enableTableColorRadioButton);
        final ButtonGroup colorPalettes = new ButtonGroup();
        colorPalettes.add(m_useColorPaletteSet1RadioButton);
        colorPalettes.add(m_useColorPaletteSet2RadioButton);
        colorPalettes.add(m_useColorPaletteSet3RadioButton);
        final GridBagConstraints colorConstraints = new GridBagConstraints();
        colorConstraints.insets = new Insets(5, 5, 5, 5);
        colorConstraints.anchor = GridBagConstraints.NORTHWEST;
        colorConstraints.gridx = 0;
        colorConstraints.gridy = 0;
        colorConstraints.weightx = 1;
        colorPanel.add(m_enableClusterColorRadioButton, colorConstraints);
        colorConstraints.gridx++;
        colorPanel.add(m_enableTableColorRadioButton, colorConstraints);
        colorConstraints.gridx = 0;
        colorConstraints.gridy++;
        colorPanel.add(new JLabel("Select a color palette:"), colorConstraints);
        colorConstraints.gridx = 0;
        colorConstraints.gridy++;
        colorPanel.add(m_useColorPaletteSet1RadioButton, colorConstraints);
        colorConstraints.gridx++;
        colorPanel.add(set1, colorConstraints);
        colorConstraints.gridx = 0;
        colorConstraints.gridy++;
        colorPanel.add(m_useColorPaletteSet2RadioButton, colorConstraints);
        colorConstraints.gridx++;
        colorPanel.add(ColorPaletteUtil.getColorPaletteSet2AsComponent(), colorConstraints);
        colorConstraints.gridx = 0;
        colorConstraints.gridy++;
        colorPanel.add(m_useColorPaletteSet3RadioButton, colorConstraints);
        colorConstraints.gridx++;
        colorPanel.add(ColorPaletteUtil.getColorPaletteSet3AsComponent(), colorConstraints);
        colorConstraints.gridx = 0;
        colorConstraints.gridy++;

        gbc.gridx = 0;
        gbc.gridy++;
        */

        return p;
    }

    private Component interactivityPanel() {
        final JPanel p = new JPanel(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridy++;

        // View Edit Controls
        final JPanel viewControlsPanel = new JPanel(new GridBagLayout());
        viewControlsPanel.setBorder(BorderFactory.createTitledBorder("View edit controls"));
        p.add(viewControlsPanel, gbc);
        final GridBagConstraints viewControlsConstraints = new GridBagConstraints();
        viewControlsConstraints.insets = new Insets(5, 5, 5, 5);
        viewControlsConstraints.anchor = GridBagConstraints.NORTHWEST;
        viewControlsConstraints.gridx = 0;
        viewControlsConstraints.gridy = 0;
        viewControlsConstraints.weightx = 1;
        viewControlsPanel.add(m_enableViewEditCheckBox, viewControlsConstraints);
        viewControlsConstraints.gridx++;
        viewControlsPanel.add(m_enableTitleEditCheckBox, viewControlsConstraints);
        viewControlsConstraints.gridx = 0;
        viewControlsConstraints.gridy++;
        viewControlsPanel.add(m_displayFullscreenButtonCheckBox, viewControlsConstraints);
        viewControlsConstraints.gridx++;
        viewControlsPanel.add(m_enableNumClusterEditCheckBox, viewControlsConstraints);
        viewControlsConstraints.gridx = 0;
        viewControlsConstraints.gridy++;
        viewControlsPanel.add(m_enableLogScaleToggleCheckBox, viewControlsConstraints);
        viewControlsConstraints.gridx++;
        /* These options will not be implemented for the first version
        *
        viewControlsPanel.add(m_enableChangeOrientationCheckBox, viewControlsConstraints);
        viewControlsConstraints.gridx = 0;
        viewControlsConstraints.gridy++;
        */
        viewControlsPanel.add(m_enableAxisLabelEditCheckBox, viewControlsConstraints);
        viewControlsConstraints.gridx = 0;
        viewControlsConstraints.gridy++;

        gbc.gridx = 0;
        gbc.gridy++;

        // Selection
        final JPanel selectionPanel = new JPanel(new GridBagLayout());
        selectionPanel.setBorder(BorderFactory.createTitledBorder("Selection & Filtering"));
        p.add(selectionPanel, gbc);
        final GridBagConstraints selectionConstraints = new GridBagConstraints();
        selectionConstraints.insets = new Insets(5, 5, 5, 5);
        selectionConstraints.anchor = GridBagConstraints.NORTHWEST;
        selectionConstraints.gridx = 0;
        selectionConstraints.gridy = 0;
        selectionConstraints.weightx = 1;
        selectionPanel.add(m_enableSelectionCheckBox, selectionConstraints);
        selectionConstraints.gridx++;
        selectionPanel.add(m_showClearSelectionButtonCheckBox, selectionConstraints);
        selectionConstraints.gridx = 0;
        selectionConstraints.gridy++;
        selectionPanel.add(new JLabel("Selection column name: "), selectionConstraints);
        selectionConstraints.gridx++;
        selectionPanel.add(m_selectionColumnNameTextField, selectionConstraints);
        selectionConstraints.gridx = 0;
        selectionConstraints.gridy++;
        selectionPanel.add(m_publishSelectionEventsCheckBox, selectionConstraints);
        selectionConstraints.gridx++;
        selectionPanel.add(m_subscribeSelectionEventsCheckBox, selectionConstraints);
        selectionConstraints.gridx = 0;
        selectionConstraints.gridy++;
        selectionPanel.add(m_showSelectedOnlyToggleCheckBox, selectionConstraints);
        selectionConstraints.gridx++;
        selectionPanel.add(m_showSelectedOnlyCheckBox, selectionConstraints);
        selectionConstraints.gridx = 0;
        selectionConstraints.gridy++;
        selectionPanel.add(m_subscribeFilterEventsCheckBox, selectionConstraints);

        gbc.gridx = 0;
        gbc.gridy++;

        // Zoom
        final JPanel zoomPanel = new JPanel(new GridBagLayout());
        zoomPanel.setBorder(BorderFactory.createTitledBorder("Zoom & Panning"));
        p.add(zoomPanel, gbc);
        final GridBagConstraints zoomConstraints = new GridBagConstraints();
        zoomConstraints.insets = new Insets(5, 5, 5, 5);
        zoomConstraints.anchor = GridBagConstraints.NORTHWEST;
        zoomConstraints.gridx = 0;
        zoomConstraints.gridy = 0;
        zoomConstraints.weightx = 1;
        zoomPanel.add(m_enableZoomAndPanningCheckBox, zoomConstraints);
        zoomConstraints.gridx++;
        zoomPanel.add(m_showZoomResetButtonCheckBox, zoomConstraints);

        gbc.gridx = 0;
        gbc.gridy++;

        return p;
    }

    private String[] getColorPalette() {
        if (m_useColorPaletteSet1RadioButton.isSelected()) {
            return ColorPaletteUtil.PALETTE_SET1;
        }
        if (m_useColorPaletteSet2RadioButton.isSelected()) {
            return ColorPaletteUtil.PALETTE_SET2;
        }
        if (m_useColorPaletteSet3RadioButton.isSelected()) {
            return ColorPaletteUtil.PALETTE_SET3;
        }
        return null;
    }

    private void setColorPaletteRadioButtons(final String[] colorPalette) {
        m_useColorPaletteSet1RadioButton.setSelected(Arrays.equals(colorPalette, ColorPaletteUtil.PALETTE_SET1));
        m_useColorPaletteSet2RadioButton.setSelected(Arrays.equals(colorPalette, ColorPaletteUtil.PALETTE_SET2));
        m_useColorPaletteSet3RadioButton.setSelected(Arrays.equals(colorPalette, ColorPaletteUtil.PALETTE_SET3));
    }
}
