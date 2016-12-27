package hr.fer.zemris.projekt.GUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import hr.fer.zemris.projekt.algorithms.Algorithm;
import hr.fer.zemris.projekt.algorithms.ObservableAlgorithm;
import hr.fer.zemris.projekt.algorithms.Robot;
import hr.fer.zemris.projekt.algorithms.geneticProgramming.GeneticProgramming;
import hr.fer.zemris.projekt.parameter.Parameters;
import hr.fer.zemris.projekt.simulator.Simulator;

public class LearningPanel extends JPanel {

	private static final long serialVersionUID = 6104960870300948842L;
	
	private ObservableAlgorithm algorithm;
	private Robot robot;
	private Simulator simulator = new Simulator();
	
	
	private JButton btnStart = new JButton("Run Algorithm");
	private JButton btnExportRobot = new JButton("Save Robot to File");
	private JButton btnRunSimulation = new JButton("Simulate");
	

	public LearningPanel() {
		super();

		initGUI();
	}

	private void initGUI() {
		setLayout(new BorderLayout());

		JPanel algorithmOptions = new JPanel();

		ParametersPanel parameters = new ParametersPanel();
		algorithmOptions.add(parameters, BorderLayout.CENTER);

		ObservableAlgorithm[] algoritms = new ObservableAlgorithm[] { new GeneticProgramming() };
		JComboBox<ObservableAlgorithm> cbAlgoritms = new JComboBox<>(algoritms);
		add(cbAlgoritms, BorderLayout.PAGE_START);

		cbAlgoritms.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				algorithm = (ObservableAlgorithm) cbAlgoritms.getSelectedItem();
				parameters.setParameters(algorithm.getDefaultParameters());
				btnStart.setEnabled(true);
				btnExportRobot.setEnabled(false);
				btnRunSimulation.setEnabled(false);
			}
		});

		add(algorithmOptions, BorderLayout.LINE_START);

		JPanel graphicalPanel = new JPanel();
		add(graphicalPanel, BorderLayout.CENTER);

		JPanel mapEditor = new JPanel(new GridLayout(0, 1));
		add(mapEditor, BorderLayout.LINE_END);

		
		
		JSlider slMapNumber = new JSlider(10, 100, 50);
		slMapNumber.createStandardLabels(50);
		slMapNumber.setMajorTickSpacing(20);
		slMapNumber.setPaintTicks(true);
		slMapNumber.setPaintLabels(true);
		
		JLabel lMapNumber = new JLabel("Number of maps: " + slMapNumber.getValue());
		slMapNumber.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				lMapNumber.setText("Number of maps: " + slMapNumber.getValue());
				
			}
		});
		
		mapEditor.add(lMapNumber);
		mapEditor.add(slMapNumber);

		
		
		JSlider slMapSize = new JSlider(2, 15, 10);
		slMapSize.createStandardLabels(5);
		slMapSize.setMajorTickSpacing(5);
		slMapSize.setPaintTicks(true);
		slMapSize.setPaintLabels(true);
		
		JLabel lMapSize = new JLabel("Map side size: " + slMapSize.getValue());
		slMapSize.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				lMapSize.setText("Map side size: " + slMapSize.getValue());
				
			}
		});		
		mapEditor.add(lMapSize);
		mapEditor.add(slMapSize);

		
		
		JSlider slBottlePercentage = new JSlider(0, 100, 50);
		Hashtable labelTable = new Hashtable();
		labelTable.put(0, new JLabel("0"));
		labelTable.put(50, new JLabel("0.5"));
		labelTable.put(100, new JLabel("1"));
		slBottlePercentage.setLabelTable(labelTable);
		slBottlePercentage.setPaintLabels(true);
		
		JLabel lBottlePercentage = new JLabel("Percentage of bottles: " + slBottlePercentage.getValue() + "%");
		slBottlePercentage.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				lBottlePercentage.setText("Percentage of bottles: " + slBottlePercentage.getValue() + "%");
				
			}
		});
		mapEditor.add(lBottlePercentage);
		mapEditor.add(slBottlePercentage);
		
		btnExportRobot.setEnabled(false);
		btnExportRobot.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser chooser = new JFileChooser();
				int returnVal = chooser.showDialog(mapEditor, "Save");
				
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					
					try {
						algorithm.writeSolutionToFile(Paths.get(file.getPath()), robot);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
					}
				}
			}
		});
		mapEditor.add(btnExportRobot);
		
		
		
		btnRunSimulation.setEnabled(false);
		btnRunSimulation.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO run simulation tab with robot
				
			}
		});
		mapEditor.add(btnRunSimulation);
		
		btnStart.setEnabled(false);
		mapEditor.add(btnStart);		
		
		btnStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int mapNum = slMapNumber.getValue();
				int mapSide = slMapSize.getValue();
				int numOfBottles = (int) Math.round(slBottlePercentage.getValue() * 0.01 * mapSide * mapSide);
				
				SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>(){

					@Override
					protected Void doInBackground() throws Exception {
						
						simulator.generateGrids(mapNum, numOfBottles, mapSide, mapSide, false);
						robot = algorithm.run(simulator, parameters.getParameters());
						
						return null;
					}

					@Override
					protected void done() {
						//TODO
						btnExportRobot.setEnabled(true);
						btnRunSimulation.setEnabled(true);
					}
					
				};
				
				worker.execute();
				
			}
		});

	}

}
