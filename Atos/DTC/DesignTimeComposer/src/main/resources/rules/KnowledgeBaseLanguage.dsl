[condition][]new_design=d : TaggedModelEvent()
[consequence][]log "{message}"=System.out.println("{message}");
[consequence][]global requirement "{reference}" "{expression}"=a.addGlobalRequirementAsAssumption("{reference}", "{expression}");
[*][]namespace "{prefix}" "{namespace}"=agent.addNamespace ("{prefix}", "{namespace}");
[condition][]initNS=ns: Namespace()
[consequence][]template_def "{template_uri}"=GoalTemplateAdapter a = new GoalTemplateAdapter(d, agent);a.setTemplateURI("{template_uri}");
[consequence][]functionalClassification "{goal_uri}"=a.addFunctionalClassification ("{goal_uri}");
[consequence][]def_template=a.initAdapter(drools.getRule().getName());if (a.matchGoal ()){a.applyAdapter();}
[consequence][]service_def "{service_uri}"=GoalWSAdapter a = new GoalWSAdapter(d, agent); a.setServiceURI ("{service_uri}");
[consequence][]definition "{wsdl_url}"=a.setServiceDefinitionURL("{wsdl_url}");
[consequence][]operation "{operation}"=a.setServiceOperation("{operation}");
[consequence][]global constraint "{reference}" "{expression}"=a.addGlobalConstraintAsAssumption("{reference}", "{expression}");
[consequence][]global constraint "{reference}"=a.addGlobalConstraintAsAssumption("{reference}", null);
[consequence][]global requirement "{reference}"=a.addGlobalRequirementAsAssumption("{reference}", null);
[consequence][]terminology {namespace}  {uri}=agent.addTerminology({namespace}, "{uri}");
[condition][]initTerminology=term:Terminology();
[consequence][]input "{input}"=a.addInput ("{input}");
[consequence][]output "{output}"=a.addOutput ("{output}");
[consequence][]register terminologies=agent.registerTerminologies();
[consequence][]def_service=a.initAdapter(drools.getRule().getName()); if (a.matchGoal ()){ a. applyAdapter (); }
