package semicontinuity.idea.code.analyzer.graph;

public interface DAGraphImplTestData6 {

    default DAGraph<String> exampleGraph6() {
        DAGraph<String> g = new DAGraphImpl<>();
        g.addVertex("GetInstances");
        g.addVertex("GetAllInstancesByTarget");
        g.addVertex("AddDCsToRegion");
        g.addVertex("DeleteDCsFromRegion");
        g.addVertex("GetRegion");
        g.addVertex("ActivateDC");
        g.addVertex("DeactivateDC");
        g.addVertex("IsDCEnabled");
        g.addVertex("UpdateInstance");
        g.addVertex("GetConfig");
        g.addVertex("SaveConfig");
        g.addVertex("ListDeactivatedTargetsByDC");

        g.addVertex("getTarget");
        g.addVertex("processGetInstancesResult");
        g.addVertex("getDisabledDatacenterTargets");
        g.addVertex("getCurrentTime");
        g.addVertex("GetAllTargets");
        g.addVertex("getDCsFromRegion");
        g.addVertex("getRegionTarget");
        g.addVertex("getTarget");
        g.addVertex("getConfigTarget");

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
