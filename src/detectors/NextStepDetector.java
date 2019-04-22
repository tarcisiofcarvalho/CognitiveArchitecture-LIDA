package detectors;

import java.util.HashMap;
import java.util.Map;

import edu.memphis.ccrg.lida.pam.tasks.BasicDetectionAlgorithm;

public class NextStepDetector extends BasicDetectionAlgorithm {

    private final String modality = "";
    private Map<String, Object> detectorParams = new HashMap<>();

    @Override
    public void init() {
        super.init();
        detectorParams.put("mode", "nextStep");
    }

    @Override
    public double detect() {
        Boolean nextStep = (Boolean) sensoryMemory.getSensoryContent(modality, detectorParams);
        double activation = 0.0;
        if (nextStep != null) {
            if(nextStep)
                activation = 1.0;
        }
        return activation;
    }
}
