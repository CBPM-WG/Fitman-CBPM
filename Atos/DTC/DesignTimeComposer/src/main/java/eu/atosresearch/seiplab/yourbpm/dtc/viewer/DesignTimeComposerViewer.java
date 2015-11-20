/**
 * 
 * Copyright (C) 2006-2010 ATOS ORIGIN
 * http://www.atosorigin.com
 *
 * This software is free software; you can redistribute it and/or
 * modify it under the terms of the license LGPL.
 *
 * Authors :
 * Jesus Gorronogoitia. Atos Research and Innovation, Atos Origin SAE
 * @email jesus.gorronogoitia@atosresearch.eu
 * Mateusz Radzimski. Atos Research and Innovation, Atos Origin SAE
 * @email mateusz.radzimski@atosresearch.eu
 */
package eu.atosresearch.seiplab.yourbpm.dtc.viewer;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Point2D;
import java.net.URI;
import java.util.HashMap;

import javax.swing.JFrame;

import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.DataColorAction;
import prefuse.action.assignment.FontAction;
import prefuse.action.layout.GridLayout;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.action.layout.graph.NodeLinkTreeLayout;
import prefuse.activity.Activity;
import prefuse.controls.DragControl;
import prefuse.controls.PanControl;
import prefuse.controls.ToolTipControl;
import prefuse.controls.ZoomControl;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.EdgeRenderer;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.util.force.ForceSimulator;
import prefuse.visual.VisualItem;
import prefuse.visual.sort.TreeDepthItemSorter;
import eu.atosresearch.seiplab.yourbpm.datamodel.bpmn.ext.ISemanticAnnotation;
import eu.atosresearch.seiplab.yourbpm.dtc.agents.BlackBoardListener;
import eu.atosresearch.seiplab.yourbpm.dtc.blackboard.BlackBoard;
import eu.atosresearch.seiplab.yourbpm.dtc.designmodel.DesignModel;
import eu.atosresearch.seiplab.yourbpm.dtc.designmodel.DesignOperatorApplication;
import eu.atosresearch.seiplab.yourbpm.dtc.model.ModelFactory;
import eu.atosresearch.seiplab.yourbpm.dtc.model.ModelProxy;
import eu.atosresearch.seiplab.yourbpm.dtc.model.ModelFactory.MODELTYPE;

public class DesignTimeComposerViewer extends JFrame implements
		BlackBoardListener {
	BlackBoard blackBoard;
	private Graph graph;

	private Visualization visualization;

	private Display display;

	HashMap<DesignModel, Node> designModelNodes;

	HashMap<DesignOperatorApplication, Edge> designOperatorApplicationEdges;

	private static final String GRAPH_EXPRESSION = "graph";

	private static final String NODES_EXPRESSION = "graph.nodes";

	private static final String EDGES_EXPRESSION = "graph.edges";

	private static final int X_SIZE = 1000;

	private static final int Y_SIZE = 800;

	private int x;

	private int y;

	private String SELECTED = "1";

	private String UNSELECTED = "0";
	
	private enum ColorStates {
		NOT_CHECKED,
		SOLUTION, 
		IO_UNCHECKED_SOLUTION,
		INCOMPLETE_SOLUTION_SUITABLE,
		INCOMPLETE_SOLUTION_NOTSUITABLE,
		NO_SOLUTION,
	};
	
	private enum ViewerStyles {
		STATIC_VIEWER,
		DYNAMIC_VIEWER
	};

	private ForceSimulator fsim;
	
	private ViewerStyles viewerStyle = ViewerStyles.STATIC_VIEWER;
	private ModelProxy modelProxy = ModelFactory.getModelProxy(MODELTYPE.LPML_MODEL);


	public DesignTimeComposerViewer(BlackBoard blackBoard) {

		this.setTitle("Design Time Composer Visualizer");

		this.blackBoard = blackBoard;
		blackBoard.addBlackBoardListener(this);

		_initData();
		_initVisualization();
		_initRendering();
		_initActions();
		_initDisplay();
		_launch(x, y);

	}

	private void _initData() {

		this.designModelNodes = new HashMap<DesignModel, Node>();
		this.designOperatorApplicationEdges = new HashMap<DesignOperatorApplication, Edge>();
		Schema node_schema = new Schema();
		Schema edge_schema = new Schema();
		// System.out.println("data.graph.nodeKey");

		node_schema.addColumn("data.graph.nodeKey", int.class);
		node_schema.addColumn("name", String.class, "DEFAULT");
		node_schema.addColumn("selected", String.class, this.UNSELECTED);
		node_schema.addColumn("designmodel", DesignModel.class);
		node_schema.addColumn("tooltiptext", String.class);
		node_schema.addColumn("modelstatus", ColorStates.class, ColorStates.NOT_CHECKED);

		Table nodes_table = node_schema.instantiate();

		edge_schema.addColumn("data.graph.edgeKey", int.class);
		edge_schema.addColumn("data.graph.sourceKey", int.class);
		edge_schema.addColumn("data.graph.targetKey", int.class);
		edge_schema.addColumn("tooltiptext", String.class);

		Table edge_table = edge_schema.instantiate();

		graph = new Graph(nodes_table, edge_table, true,
				"data.graph.sourceKey", "data.graph.targetKey");
		// graph = new Graph(nodes_table,true);

		// Node proofNode = getANode(poofDesignModel);
		//		
		// Edge edge = graph.addEdge(seedDesignModelNode,proofNode);

	}

	private void _initVisualization() {
		this.visualization = new Visualization();
		this.visualization.add("graph", graph);

		this.visualization.setInteractive("graph.edges", null, true);

		this.visualization.setVisible("graph.edges", null, true);

	}

	private void _initRendering() {

		LabelRenderer nodeRenderer = new LabelRenderer("name");
		nodeRenderer.setRoundedCorner(3, 3); // round the corners
		nodeRenderer.setHorizontalAlignment(prefuse.Constants.CENTER);


		EdgeRenderer edgeRenderer = new EdgeRenderer();

		edgeRenderer.setArrowType(prefuse.Constants.EDGE_ARROW_FORWARD);
		edgeRenderer.setArrowHeadSize(10, 10);
		edgeRenderer.setDefaultLineWidth(5);
		this.visualization.setRendererFactory(new DefaultRendererFactory(
				nodeRenderer, edgeRenderer));

	}

	private void _initActions() {

		ActionList init = new ActionList();
		ActionList animate = new ActionList(Activity.INFINITY);
		
		if (viewerStyle == ViewerStyles.DYNAMIC_VIEWER) {
			//create dynamic layout
			init.add(new GridLayout(NODES_EXPRESSION));
			init.add(new ForceDirectedLayout(NODES_EXPRESSION));
			ForceDirectedLayout fdl = new ForceDirectedLayout(GRAPH_EXPRESSION,
					true);

			this.fsim = fdl.getForceSimulator();
			this.fsim.getForces()[0].setParameter(0, -40.0f);
			animate.add(fdl);	
			
		} else {
			//create static layout
			NodeLinkTreeLayout treeLayout = new NodeLinkTreeLayout("graph", 
				      0, 50.0D, 5.0D, 10.0D);
			treeLayout.setLayoutAnchor(new Point2D.Double(50.0D, 300.0D));
			
			init.add(treeLayout);
			init.add(new RepaintAction());
			animate.add(treeLayout);
		}


		ActionList draw = new ActionList();
		// draw.add(filter);

		//colors of different model states
		int[] palette = new int[] { 
				ColorLib.color(Color.LIGHT_GRAY), 	//NOT_CHECKED					
				ColorLib.color(Color.GREEN), 		//SOLUTION
				ColorLib.color(Color.YELLOW), 		//IO_UNCHECKED_SOLUTION
				ColorLib.color(Color.BLUE),			//INCOMPLETE_SOLUTION_SUITABLE
				ColorLib.color(Color.CYAN),			//INCOMPLETE_SOLUTION_NOTSUITABLE
				ColorLib.color(Color.BLACK)			//NO_SOLUTION				
				};

		DataColorAction fill = new DataColorAction("graph.nodes", "modelstatus",
				Constants.NOMINAL, VisualItem.FILLCOLOR, palette);

		//Object[] values = { this.UNSELECTED, this.SELECTED };
		Object[] values = { 
				ColorStates.NOT_CHECKED,
				ColorStates.SOLUTION, 
				ColorStates.IO_UNCHECKED_SOLUTION,
				ColorStates.INCOMPLETE_SOLUTION_SUITABLE,
				ColorStates.INCOMPLETE_SOLUTION_NOTSUITABLE,
				ColorStates.NO_SOLUTION
		};
		fill.setOrdinalMap(values);

		draw.add(fill);
		//		
		// draw.add(new ColorAction(NODES_EXPRESSION, VisualItem.FILLCOLOR,
		// ColorLib.rgb(200, 200, 255)));
		draw.add(new ColorAction(EDGES_EXPRESSION, VisualItem.STROKECOLOR,
				ColorLib.color(Color.BLACK)));
		draw.add(new ColorAction(EDGES_EXPRESSION, VisualItem.FILLCOLOR,
				ColorLib.color(Color.BLACK)));
		draw.add(new ColorAction(NODES_EXPRESSION, VisualItem.STROKECOLOR,
				ColorLib.color(Color.BLACK)));
		draw.add(new ColorAction(NODES_EXPRESSION, VisualItem.TEXTCOLOR,
				ColorLib.color(Color.WHITE)));

     

		draw.add(new FontAction(NODES_EXPRESSION, new Font("Tahoma", 0, 18)));

		draw.add(fill);

		//
		// ColorAction fill = new ColorAction(NODES_EXPRESSION,
		// VisualItem.FILLCOLOR, ColorLib.rgb(200, 200, 255));
		// fill.add("_fixed", ColorLib.rgb(255, 100, 100));
		// fill.add("_highlight", ColorLib.rgb(255, 200, 125));



		animate.add(new RepaintAction());

		ActionList select = new ActionList(ActionList.INFINITY);
		select.add(new ColorAction(NODES_EXPRESSION, VisualItem.FILLCOLOR,
				ColorLib.color(Color.BLACK)));
		ActionList repaint = new ActionList(ActionList.INFINITY);
		repaint.add(new RepaintAction());

		// finally, we register our ActionList with the Visualization.
		// we can later execute our Actions by invoking a method on our
		// Visualization, using the name we've chosen below.

		visualization.putAction("select", select);
		visualization.putAction("init", init);
		visualization.putAction("draw", draw);
		visualization.putAction("layout", animate);
		visualization.putAction("repaint", repaint);
		visualization.runAfter("draw", "layout");

	}

	public void _initDisplay() {
		display = new Display(visualization);
		display.setSize(X_SIZE, Y_SIZE); // set display size
		display.setDoubleBuffered(true);
		display.setHighQuality(true);

		// drag individual items around
		display.addControlListener(new DragControl());
		// pan with left-click drag on background
		display.addControlListener(new PanControl(false));
		// zoom with right-click drag
		display.addControlListener(new ZoomControl());

		// display.addControlListener(new NeighborHighlightControl());
		display.addControlListener(new ToolTipControl("tooltiptext"));
		// custom control that calls external script when click event is triggered
		display.addControlListener(new ClickControlAdapter());
		this.display.setItemSorter(new TreeDepthItemSorter());

	}

	public void _launch(int x, int y) {
		// create a new window to hold the visualization
		// JFrame frame = new JFrame("prefuse example");
		// ensure application exits when window is closed
		// this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		try {
			this.setContentPane(display);
			// show the window
			//visualization.run("init");
			//visualization.run("draw");
			this.setBounds(x, y, this.X_SIZE, this.Y_SIZE);
			this.setFocusable(true);
			this.setResizable(true);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setVisible(true);
			this.pack();
		} catch (Exception e) {
			
		}
	}

	public boolean evaluate(BlackBoard blackBoard) {
		// TODO Auto-generated method stub
		return false;
	}


	public void notify(DesignModel oldDesignModel, DesignModel newDesignModel,
			URI newDesignOperator, String dma, String adapter, String goal, String ruleName) {
		
		//we can be notified when initial model is posted on the BB.  
		if (newDesignModel == null) {
			//we are notified on seed model and we should be able to visualise it.
			//but we don't need to create an edge (there is only 1 model on the BB)
			Node oldDesignModelNode = getANode(oldDesignModel);
			
			this.visualization.run("draw");
			this.visualization.run("layout");
			
		} else {
			//this is normal situation: a model is posted as an update of some previous model.
			//we put this model and create edge between old and new. 
			Node oldDesignModelNode = getANode(oldDesignModel);
			Node newDesignModelNode = getANode(newDesignModel);
			Edge designOperatorApplicationEdge = graph.addEdge(oldDesignModelNode,
					newDesignModelNode);
			
			designOperatorApplicationEdge
					.set("tooltiptext", 
							"<html><b> DMA:</b> " + dma
							+ "<br><b> Adapter: </b>" + adapter
							+ "<br><b> Goal: </b>" + goal 
							+ "<br><b> Rule: </b>" + ruleName);
	
			this.visualization.run("draw");
			this.visualization.run("layout");
		}
	}
	
	@Override
	public void notify(DesignModel designModel) {
		Node node = this.designModelNodes.get(designModel);
		if (node == null){
			return;
		}
		
		update(designModel);

		this.visualization.run("draw");
		this.visualization.run("layout");
	}

	private void update (DesignModel designModel) {
		Node node = this.designModelNodes.get(designModel);
		if (node != null){
			//String[] chunks = designModel.getIdentifier().getFragment().split("\\-");
			String[] chunks = designModel.getIdentifier().toString().split("-");
			StringBuilder output = new StringBuilder();
			output.append("<html><center>");
			output.append("<center><b>ID:</b>");
			for(int i=1;i<chunks.length;i++){
				output.append(chunks[i]);
				output.append("-");
			}
			output.deleteCharAt(output.length()-1);
			output.append("<br><b>Status: </b>");
			output.append(designModel.getStatus().toString());
			output.append("<br><b>Assignments: </b>");                        
			for (ISemanticAnnotation sa: designModel.getAssignmentSet().getAssignments()){
				output.append("<br>" + sa.getReferenceURI() + "<br>");
			}
			output.append("]");
			
			output.append("<br><b>Specification:</b>");
			output.append("Requirements:<br>[");
			output.append(modelProxy.getRequirements(designModel.getDesignStructure()));
			output.append("] <br> Preferences <br>:");
			output.append(modelProxy.getPreferences(designModel.getDesignStructure()));
			output.append("]");
			node.set("tooltiptext",output.toString());
			
			//unchecked model
			if (designModel.getStatus().isNotChecked()) {
				node.set("modelstatus", ColorStates.NOT_CHECKED);
			}
			
			//possibilities for solution model
			if (designModel.getStatus().isSolution()) {
				node.set("modelstatus", ColorStates.SOLUTION);
			}
			
			//possibilities for incomplete model
			if (designModel.getStatus().isIOUncheckedSolution()) {
				node.set("modelstatus", ColorStates.IO_UNCHECKED_SOLUTION);
			}
			
			if (designModel.getStatus().isIncompleteSolution() && designModel.getStatus().isSuitable()) {
				node.set("modelstatus", ColorStates.INCOMPLETE_SOLUTION_SUITABLE);
			}
			
			if (designModel.getStatus().isIncompleteSolution() && designModel.getStatus().isNotSuitable()) {
				node.set("modelstatus", ColorStates.INCOMPLETE_SOLUTION_NOTSUITABLE);
			}
			
			//No solution
			if (designModel.getStatus().isNoSolution()) {
				node.set("modelstatus", ColorStates.NO_SOLUTION);
			}
					
		}
	}
	
	private Node getANode(DesignModel designModel) {
		Node node = null;

		node = this.designModelNodes.get(designModel);
		if (node == null) {
			node = graph.addNode();
			this.designModelNodes.put(designModel, node);
			node.set("name", "design-" + designModel.getIndex());
			node.set("selected", this.UNSELECTED);
			node.set("modelstatus", ColorStates.NOT_CHECKED);
			node.set("designmodel", designModel);
			String[] chunks = designModel.getIdentifier().toString().split("-"); //.getFragment().split("\\-");
			StringBuilder output = new StringBuilder();
			output.append("<html><center>");
			output.append("<b>ID:</b>");
			for(int i=1;i<chunks.length;i++){
				output.append(chunks[i]);
				output.append("-");
			}
			output.deleteCharAt(output.length()-1);
			output.append("<br><b>Status: </b>");
			output.append(designModel.getStatus().toString());
			output.append("<br><b>Assignments: </b>");
			output.append(designModel.getAssignmentSet().getAssignments());
			output.append("<br><b>Specification: </b>");
			output.append(modelProxy.getRequirements(designModel.getDesignStructure()));
			node.set("tooltiptext",output.toString());
			//System.out.println(" > " + nodeNumber);
		}
		return node;
	}

	@Override
	public void clean() {
		_initData();
		_initVisualization();
		_initRendering();
		_initActions();
		_initDisplay();
		_launch(x, y);
	}

}
