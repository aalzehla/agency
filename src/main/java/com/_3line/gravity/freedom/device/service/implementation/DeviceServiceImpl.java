package com._3line.gravity.freedom.device.service.implementation;

import com._3line.gravity.freedom.agents.models.Devicesetup;
import com._3line.gravity.freedom.agents.repository.DeviceSetupRepository;
import com._3line.gravity.freedom.device.model.DeviceAudit;
import com._3line.gravity.freedom.device.repository.DeviceAuditRepository;
import com._3line.gravity.freedom.device.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Service;
import sun.management.resources.agent;


@Service
public class DeviceServiceImpl implements DeviceService {


    @Autowired
    DeviceSetupRepository  deviceSetupRepository;

    @Autowired
    DeviceAuditRepository deviceAuditRepository;


    @Override
    public void auditLoginDevice(Devicesetup device) {
        DeviceAudit deviceAudit = deviceAuditRepository.
                findByAgentIdentifierAndDeviceId(String.valueOf(device.getAgentId()),device.getDeviceid());
        if(deviceAudit==null){
            deviceAudit = new DeviceAudit();
            deviceAudit.setAgentIdentifier(String.valueOf(device.getAgentId()));
            deviceAudit.setDeviceId(device.getDeviceid());
            deviceAuditRepository.save(deviceAudit);
        }
    }

    @Override
    public Devicesetup fetchAuditByDeviceIdAndAgentId(String deviceId, String agentId) {
        return null;
    }

    @Override
    public Boolean hasLoggedInBefore(String deviceId, long agentId) {
        DeviceAudit deviceAudit = deviceAuditRepository.
                findByAgentIdentifierAndDeviceId(String.valueOf(agentId),deviceId);
        Devicesetup  devicesetup;
        if(deviceAudit==null){
            devicesetup = deviceSetupRepository.findByDeviceidAndAgentId(deviceId,agentId);
            if(devicesetup==null){
                return false;
            }else{
                return true;
            }
        }else{
            return true;
        }
    }



    @Override
    public Devicesetup attacheDeviceToUser(String deviceId,long agentId) {

        Devicesetup devicesetup = deviceSetupRepository.findByAgentId(agentId);
        if(devicesetup==null){
            devicesetup = new Devicesetup();
            devicesetup.setDeviceid(deviceId);
            devicesetup.setAgentId(agentId);
            devicesetup.setStatus(0);
        }else{
            devicesetup.setDeviceid(deviceId);
        }

        devicesetup = deviceSetupRepository.save(devicesetup);
        auditLoginDevice(devicesetup);
        return devicesetup;
    }

    @Override
    public Devicesetup fetchByDeviceIdAndAgentId(String deviceId, long agentId) {
        Devicesetup devicesetup = deviceSetupRepository.findByDeviceidAndAgentId(deviceId,agentId);
        if(devicesetup==null){
            return null;
        }else{
            return devicesetup;
        }
    }
}
