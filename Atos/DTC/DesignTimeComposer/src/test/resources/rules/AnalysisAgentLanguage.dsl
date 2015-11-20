[condition][]new design=d : NewDesignModelEvent();
[consequence][]check design is complete=agent.checkModelCompletness(d);
[consequence][]check design is admissible=agent.checkModelAdmissibility(d);
[consequence][]check design is suitable=agent.checkModelSuitability(d);
[consequence][]notify analysis=agent.notifyAnalysisAgentResponse(d);
[*][]Log "{message}"=System.out.println("{message}");
[consequence][]check design is io compatibile=agent.isDataFlowAdded(d);
