package detectors;

import java.util.HashMap;
import java.util.Map;

import edu.memphis.ccrg.lida.pam.tasks.BasicDetectionAlgorithm;

public class GoalCompletedDetector extends BasicDetectionAlgorithm {

    private final String modality = "";
    private Map<String, Object> detectorParams = new HashMap<>();

    @Override
    public void init() {
        super.init();
        detectorParams.put("mode", "goalCompleted");
    }

    @Override
    public double detect() {
        Boolean goalCompleted = (Boolean) sensoryMemory.getSensoryContent(modality, detectorParams);
        double activation = 0.0;
        if (goalCompleted != null) {
            if(goalCompleted)
                activation = 1.0;
        }
        return activation;
    }
}
