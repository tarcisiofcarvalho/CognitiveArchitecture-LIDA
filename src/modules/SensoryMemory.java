package modules;

import edu.memphis.ccrg.lida.sensorymemory.SensoryMemoryImpl;
import java.util.HashMap;
import java.util.Map;

public class SensoryMemory extends SensoryMemoryImpl {

    private Map<String, Object> sensorParam;
    private Boolean thingAhead;
    private Boolean nextStep;
    private Boolean goalCompleted;

    public SensoryMemory() {
        this.sensorParam = new HashMap<>();
        this.thingAhead = false;
        this.nextStep = false;
        this.goalCompleted = false;
    }

    @Override
    public void runSensors() {

        // -- Goal completed sensor setup -- //
        sensorParam.clear();
    	sensorParam.put("mode", "goalCompleted");
        goalCompleted = (Boolean) environment.getState(sensorParam);
        
        // -- Brick Ahead sensor setup -- //
        sensorParam.clear();
    	sensorParam.put("mode", "thingAhead");
        thingAhead = (Boolean) environment.getState(sensorParam);

        // -- Next step sensor setup -- //
        sensorParam.clear();
        sensorParam.put("mode", "nextStep");
        nextStep = (Boolean) environment.getState(sensorParam);  
        
    }

    @Override
    public Object getSensoryContent(String modality, Map<String, Object> params) {
        Object requestedObject = null;
        String mode = (String) params.get("mode");
        switch (mode) {

        	case "goalCompleted":
                requestedObject = goalCompleted;
                break;
        	
        	case "thingAhead":
        		requestedObject = thingAhead;
        		break;
            
        	case "nextStep":
                requestedObject = nextStep;
                break;                
            default:
                break;
        }
        return requestedObject;
    }

    @Override
    public Object getModuleContent(Object... os) {
        return null;
    }

    @Override
    public void decayModule(long ticks) {
    }
}
