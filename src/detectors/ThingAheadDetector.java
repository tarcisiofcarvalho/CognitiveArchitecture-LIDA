package detectors;

import java.util.HashMap;
import java.util.Map;

import edu.memphis.ccrg.lida.pam.tasks.BasicDetectionAlgorithm;

public class ThingAheadDetector extends BasicDetectionAlgorithm {

    private final String modality = "";
    private Map<String, Object> detectorParams = new HashMap<>();

    @Override
    public void init() {
        super.init();
        detectorParams.put("mode", "thingAhead");
    }

    @Override
    public double detect() {
        Boolean thingAhead = (Boolean) sensoryMemory.getSensoryContent(modality, detectorParams);
        double activation = 0.0;
        if (thingAhead != null) {
            if(thingAhead)
                activation = 1.0;
            // DEBUG:
            //System.out.println("thingAhead activated.....");
        }
        return activation;
    }
}
