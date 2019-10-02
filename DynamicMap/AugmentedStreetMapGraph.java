package bearmaps;

import bearmaps.utils.graph.streetmap.StreetMapGraph;
import bearmaps.utils.graph.streetmap.Node;
import bearmaps.utils.ps.KDTree;
import bearmaps.utils.ps.TrieSet;
import bearmaps.utils.ps.Point;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 * @author Alan Yao, Josh Hug, ________
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {

    private HashMap<Point, Long> pointMap;
    private TrieSet nameMap;
    private KDTree kdTree;

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);

        // Part II
        pointMap = new HashMap<>();
        nameMap = new TrieSet();
        for (Node node : getNodes()) {
            if (!neighbors(node.id()).isEmpty()) {
                pointMap.put(new Point(node.lon(), node.lat()), node.id());
            }

            String currName = name(node.id());
            if (currName != null) {
                HashMap<String, Object> currLoc = new HashMap<>();
                currLoc.put("lat", node.lat());
                currLoc.put("lon", node.lon());
                currLoc.put("name", currName);
                currLoc.put("id", node.id());
                String cleanName = cleanString(currName);
                nameMap.addLocation(cleanName, currLoc);
            }

        }

        // Part II
        List<Point> pointList = new LinkedList<>(pointMap.keySet());
        kdTree = new KDTree(pointList);
    }


    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     *
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        return pointMap.get(kdTree.nearest(lon, lat));
    }


    /**
     * For Project Part III (extra credit)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     *
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        return nameMap.keysWithPrefix(cleanString(prefix));
    }

    /**
     * For Project Part III (extra credit)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     *
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        return nameMap.getLoc(cleanString(locationName));
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     *
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

}
