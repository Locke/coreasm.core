/*
 * GraphPlugin.java
 *
 * Copyright (C) 2010 Roozbeh Farahbod
 *
 * Last modified by $Author$ on $Date$.
 *
 * Licensed under the Academic Free License version 3.0
 *   http://www.opensource.org/licenses/afl-3.0.php
 *   http://www.coreasm.org/afl-3.0.php
 *
 */
package org.coreasm.network.plugins.graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jparsec.Parser;
import org.jparsec.Parsers;

import org.coreasm.engine.VersionInfo;
import org.coreasm.engine.absstorage.BackgroundElement;
import org.coreasm.engine.absstorage.Element;
import org.coreasm.engine.absstorage.ElementBackgroundElement;
import org.coreasm.engine.absstorage.Enumerable;
import org.coreasm.engine.absstorage.FunctionElement;
import org.coreasm.engine.absstorage.RuleElement;
import org.coreasm.engine.absstorage.UniverseElement;
import org.coreasm.engine.interpreter.ASTNode;
import org.coreasm.engine.interpreter.Interpreter;
import org.coreasm.engine.interpreter.Node;
import org.coreasm.engine.kernel.KernelServices;
import org.coreasm.engine.parser.GrammarRule;
import org.coreasm.engine.parser.ParserTools;
import org.coreasm.engine.plugin.InterpreterPlugin;
import org.coreasm.engine.plugin.ParserPlugin;
import org.coreasm.engine.plugin.Plugin;
import org.coreasm.engine.plugin.VocabularyExtender;
import org.coreasm.engine.plugins.set.SetBackgroundElement;
import org.coreasm.engine.plugins.set.SetElement;
import org.coreasm.util.Logger;

/**
 * A plugin to provide the Graph background.
 *
 * @author Roozbeh Farahbod
 *
 */
public class GraphPlugin extends Plugin implements VocabularyExtender, ParserPlugin, InterpreterPlugin {

	public static final VersionInfo VERSION_INFO = new VersionInfo(1, 1, 1, "alpha");

	public static final String PLUGIN_NAME = GraphPlugin.class.getSimpleName();

	public static final String VERTICES_FUNC_NAME = "vertices";
	public static final String EDGES_FUNC_NAME = "edges";
	public static final String SRC_VERTEX_FUNC_NAME = "sourceVertex";
	public static final String TRG_VERTEX_FUNC_NAME = "targetVertex";
	public static final String TO_GRAPH_FUNC_NAME = "toGraph";
	public static final String CREATE_GRAPH_FUNC_NAME = "createGraph";
	public static final String NEW_EDGE_TERM_NAME = "NewEdgeTerm";
	public static final String ADD_VERTEX_GR_NAME = "AddGraphVertexRule";

	public static final String NEW_EDGE_KW_NAME = "newedge";


	private final String[] keywords = {NEW_EDGE_KW_NAME};
	private final String[] operators = {};

	private Map<String, FunctionElement> functions = null;
	private Map<String, BackgroundElement> backgrounds = null;
	private Map<String, GrammarRule> parsers;

	private HashSet<String> dependencies;

	@Override
	public Set<String> getDependencyNames() {
		if (dependencies == null) {
			dependencies = new HashSet<String>();
			dependencies.add("SetPlugin");
			dependencies.add("ListPlugin");
			dependencies.add("CollectionPlugin");
		}
		return dependencies;
	}

	public static Graph<Element, Element> createDefaultGraph() {
		return new DefaultDirectedGraph<>(() -> Element.UNDEF, () -> new EdgeElement(Element.UNDEF, Element.UNDEF), false);
	}

	public GraphPlugin() {}

	@Override
	public void initialize() {
	}

	@Override
	public VersionInfo getVersionInfo() {
		return VERSION_INFO;
	}

	@Override
	public Set<String> getBackgroundNames() {
		return getBackgrounds().keySet();
	}

	@Override
	public Map<String, BackgroundElement> getBackgrounds() {
		if (backgrounds == null) {
			backgrounds = new HashMap<String, BackgroundElement>();
			backgrounds.put(GraphBackgroundElement.BACKGROUND_NAME, new GraphBackgroundElement());
			backgrounds.put(EdgeBackgroundElement.BACKGROUND_NAME, new EdgeBackgroundElement());
		}
		return backgrounds;
	}

	@Override
	public Set<String> getFunctionNames() {
		return functions.keySet();
	}

	@Override
	public Map<String, FunctionElement> getFunctions() {
		if (functions == null) {
			functions = new HashMap<String, FunctionElement>();

			// vertices
			functions.put(VERTICES_FUNC_NAME, new GraphAttributeFunctionElement() {

				@Override
				public Element getValue(GraphElement ge) {
					return new SetElement(ge.getGraph().vertexSet());
				}

				@Override
				public String getResultBackground() {
					return SetBackgroundElement.SET_BACKGROUND_NAME;
				}
			});

			// edges
			functions.put(EDGES_FUNC_NAME, new GraphAttributeFunctionElement() {

				@Override
				public Element getValue(GraphElement ge) {
					return new SetElement(ge.getGraph().edgeSet());
				}

				@Override
				public String getResultBackground() {
					return SetBackgroundElement.SET_BACKGROUND_NAME;
				}
			});

			// sourceVertex
			functions.put(SRC_VERTEX_FUNC_NAME, new EdgeAttributeFunctionElement() {

				@Override
				public Element getValue(EdgeElement ge) {
					return ge.source;
				}

				@Override
				public String getResultBackground() {
					return ElementBackgroundElement.ELEMENT_BACKGROUND_NAME;
				}
			});

			// targetVertex
			functions.put(TRG_VERTEX_FUNC_NAME, new EdgeAttributeFunctionElement() {

				@Override
				public Element getValue(EdgeElement ge) {
					return ge.target;
				}

				@Override
				public String getResultBackground() {
					return ElementBackgroundElement.ELEMENT_BACKGROUND_NAME;
				}
			});

			ToGraphFunctionElement tgfe = new ToGraphFunctionElement();
			// toGraph
			functions.put(TO_GRAPH_FUNC_NAME, tgfe);

			// createGraph
			functions.put(CREATE_GRAPH_FUNC_NAME, tgfe);

			// dijkstra
			functions.put(DijkstraShortestPathFunctionElement.FUNCTION_NAME, new DijkstraShortestPathFunctionElement());

			ConnectivityInspectorCache inspectorCache = new ConnectivityInspectorCache();

			// connected set
			functions.put(ConnectedSetFunctionElement.FUNCTION_NAME, new ConnectedSetFunctionElement(inspectorCache));

			// isConnected
			functions.put(IsConnectedFunctionElement.FUNCTION_NAME, new IsConnectedFunctionElement(inspectorCache));

			CycleDetectorCache detectorCache = new CycleDetectorCache();

			// hasCycle
			functions.put(HasCyclesFunctionElement.FUNCTION_NAME, new HasCyclesFunctionElement(detectorCache));

			// findCyclesWithVertex
			functions.put(FindCyclesFunctionElement.FUNCTION_NAME, new FindCyclesFunctionElement(detectorCache));

			// subgraph
			functions.put(SubGraphFunctionElement.FUNCTION_NAME, new SubGraphFunctionElement());

			// asUndirectedGraph
			functions.put(AsUndirectedFunctionElement.FUNCTION_NAME, new AsUndirectedFunctionElement());
		}
		return functions;
	}

	@Override
	public Set<String> getRuleNames() {
		return getRules().keySet();
	}

	@Override
	public Map<String, RuleElement> getRules() {
		return Collections.emptyMap();
	}

	@Override
	public Set<String> getUniverseNames() {
		return getUniverses().keySet();
	}

	@Override
	public Map<String, UniverseElement> getUniverses() {
		return Collections.emptyMap();
	}

	@Override
	public String[] getKeywords() {
		return keywords;
	}

	@Override
	public Set<Parser<?>> getLexers() {
		return Collections.emptySet();
	}

	@Override
	public String[] getOperators() {
		return operators;
	}

	@Override
	public Parser<Node> getParser(String nonterminal) {
		return null;
	}

	@Override
	public Map<String, GrammarRule> getParsers() {
		if (parsers == null) {
			parsers = new HashMap<String, GrammarRule>();

			KernelServices kernel = (KernelServices)capi.getPlugin("Kernel").getPluginInterface();

			Parser<Node> termParser = kernel.getTermParser();

			ParserTools pTools = ParserTools.getInstance(capi);

			// NewEdgeTerm : 'newedge' Term
			Parser<Node> newEdgeParser = Parsers.array(
					new Parser[] {
					pTools.getKeywParser(NEW_EDGE_KW_NAME, PLUGIN_NAME),
					termParser}).map(
					new ParserTools.ArrayParseMap(PLUGIN_NAME) {

						@Override
						public Node apply(Object[] nodes) {
							NewEdgeNode node = new NewEdgeNode(((Node)nodes[0]).getScannerInfo());
							addChildren(node, nodes);
							return node;
						}

					});

			parsers.put("BasicTerm", new GrammarRule(NEW_EDGE_TERM_NAME, "'" + NEW_EDGE_KW_NAME + "' Term", newEdgeParser, PLUGIN_NAME));

		}

		return parsers;
	}

	@Override
	public ASTNode interpret(Interpreter interpreter, ASTNode pos) {

		// newedge
		if (pos instanceof NewEdgeNode) {
			NewEdgeNode ne = (NewEdgeNode)pos;
			if (!ne.getVertices().isEvaluated())
				return ne.getVertices();

			Element v = ne.getVertices().getValue();
			if (v != null && v instanceof Enumerable) {
				Enumerable vs = (Enumerable)v;
				if (vs.size() == 2) {
					pos.setNode(null, null, new EdgeElement(vs.enumerate()));
					return pos;
				}
			}

			// if we are here, we didn't have a tuple as vertices
			String msg = "'" + NEW_EDGE_KW_NAME + "' requires a collection of two vertices to create a new edge";
			capi.error(msg, pos, interpreter);
			Logger.log(Logger.ERROR, Logger.plugins, msg);
		}

		return pos;
	}
}
