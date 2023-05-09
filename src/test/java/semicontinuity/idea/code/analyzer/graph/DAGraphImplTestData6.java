package semicontinuity.idea.code.analyzer.graph;

public interface DAGraphImplTestData6 {

    default DAGraph<String> exampleGraph6() {
        DAGraph<String> g = new DAGraphImpl<>();
        g.addNode("GetInstances");
        g.addNode("GetAllInstancesByTarget");
        g.addNode("AddDCsToRegion");
        g.addNode("DeleteDCsFromRegion");
        g.addNode("GetRegion");
        g.addNode("ActivateDC");
        g.addNode("DeactivateDC");
        g.addNode("IsDCEnabled");
        g.addNode("UpdateInstance");
        g.addNode("GetConfig");
        g.addNode("SaveConfig");
        g.addNode("ListDeactivatedTargetsByDC");

        g.addNode("getTarget");
        g.addNode("processGetInstancesResult");
        g.addNode("getDisabledDatacenterTargets");
        g.addNode("getCurrentTime");
        g.addNode("GetAllTargets");
        g.addNode("getDCsFromRegion");
        g.addNode("getRegionTarget");
        g.addNode("getTarget");
        g.addNode("getConfigTarget");

        g.addEdge("GetInstances", "processGetInstancesResult");
        g.addEdge("GetInstances", "getTarget");
        g.addEdge("GetAllInstancesByTarget", "processGetInstancesResult");
        g.addEdge("GetAllInstancesByTarget", "GetAllTargets");

        g.addEdge("AddDCsToRegion", "getRegionTarget");
        g.addEdge("DeleteDCsFromRegion", "getRegionTarget");
        g.addEdge("GetRegion", "getRegionTarget");
        g.addEdge("GetRegion", "getDCsFromRegion");

        g.addEdge("ActivateDC", "getTarget");
        g.addEdge("DeactivateDC", "getTarget");
        g.addEdge("IsDCEnabled", "getTarget");
        g.addEdge("UpdateInstance", "getTarget");
        g.addEdge("UpdateInstance", "getCurrentTime");

        g.addEdge("GetConfig", "getConfigTarget");
        g.addEdge("SaveConfig", "getConfigTarget");

        g.addEdge("processGetInstancesResult", "getDisabledDatacenterTargets");
        g.addEdge("processGetInstancesResult", "getCurrentTime");
        return g;
    }
}
