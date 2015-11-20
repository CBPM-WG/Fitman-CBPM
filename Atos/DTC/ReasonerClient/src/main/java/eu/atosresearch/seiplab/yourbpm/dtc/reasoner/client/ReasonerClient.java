package eu.atosresearch.seiplab.yourbpm.dtc.reasoner.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eu.atosresearch.seiplab.yourbpm.dtc.reasoner.wsl.types.WSLElement;

public interface ReasonerClient {
//	public HashMap<String, List<String>> checkNFPIncompatibility(
//			List<String> localNFP, List<String> globalNFP); //There is only a WSML based implementation
	public List<? extends WSLElement> doMSMServiceMatch(List<String> FC,
			List<String> NFP, HashMap<String, ArrayList<String>> input,
			HashMap<String, ArrayList<String>> output, List<String> context,
			boolean isService, int maxFetched);
	public String executeQuery(String str);
	public List<POSMResult> retrieveServiceDetails(String query);
	public boolean matchClasses(String c1, String c2);
	public boolean matchClasses(String[] c1s, String[] c2s);
}
