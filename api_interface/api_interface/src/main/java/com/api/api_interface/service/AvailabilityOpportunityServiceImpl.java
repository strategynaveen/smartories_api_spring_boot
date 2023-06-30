package com.api.api_interface.service;

import com.api.api_interface.entity.*;
import com.api.api_interface.repo.*;
import com.api.api_interface.repo.MachineValuesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Component("availabilityOpportunity")
public class AvailabilityOpportunityServiceImpl implements AvailabilityOpportunityService{
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	RawDataFilterServeice rawDataFilterServeice;

//	opportunity drilldown graph  get the reasons and reasons wise total
	@Autowired
	private AvailabilityOpportunityServiceImpl availability_service;

	protected  List get_availability_reason_wise_total;

	protected  List get_availability_reasons;


	public Map getAvailabilityOpportunityValues(){

		List<MachineValuesEntity> machineList = userServiceImpl.machineValues;
		List<DowntimeReasonEntity> downReason = userServiceImpl.downtimeReason;
		List<DowntimeRawDataEntity> downData = userServiceImpl.downtimeFilter;


		float GrandTotal=0;
		List finalList = new ArrayList();
		for (MachineValuesEntity machine :machineList ) {
			List ar = new ArrayList();
			for(DowntimeReasonEntity reason: downReason){
				float reasonValue=0;
				float splitDuration=0;
				for(DowntimeRawDataEntity rawData: downData){
					if ((machine.getMachine_id().equals(rawData.getMachine_id())) && reason.getDowntime_reason_id().equals(rawData.getDowntime_reason_id())) {
						if (reason.getDowntime_category().equals("Planned") && reason.getDowntime_reason().equals("Machine OFF")){
							reasonValue = reasonValue +((machine.getMachine_offrate_per_hour()+rawData.getSplit_duration())/60);
						}
						else {
							reasonValue = reasonValue +((machine.getRate_per_hour()+rawData.getSplit_duration())/60);
						}
						splitDuration=splitDuration+rawData.getSplit_duration();
					}
				}
				Map t = new HashMap();
				t.put("machine_id",machine.getMachine_id());
				t.put("reason_id",reason.getDowntime_reason_id());
				t.put("reason",reason.getDowntime_reason());
				t.put("machine_name",machine.getMachine_name());
				t.put("oppCost",reasonValue);
				t.put("duration",splitDuration);

				GrandTotal = GrandTotal+reasonValue;
				ar.add(t);
			}
			finalList.add(ar);
		}

		int l = downReason.size();
		int l1= finalList.size();

		List reasonTotal = new ArrayList();
		List durationTotal = new ArrayList();

//		System.out.println(finalList.get(0).get(0));
		for (int i=0; i < l; i++) {
			float total = 0;
			float duration = 0;
			for (int j = 0; j < l1; j++) {
				List x = (List) finalList.get(j);
				Map y = (Map) x.get(i);
				total = total + (float) y.get("oppCost");
				duration = duration + (float) y.get("duration");
			}
			reasonTotal.add((int) total);
			durationTotal.add((int) duration);
		}

		int len = reasonTotal.size();
		for (int i=0;i<(len-1);i++){
			int min_index = i;
			for (int j = j=i+1; j < len; j++){
				if ((int)reasonTotal.get(j) > (int)reasonTotal.get(min_index)){
					min_index = j;
				}
			}
			int temp = (int)reasonTotal.get(i);
			reasonTotal.set(i, reasonTotal.get(min_index));
			reasonTotal.set(min_index, temp);

			int s= machineList.size();
			for (int k=0;k<s;k++){
				List x = (List) finalList.get(k);
				Map y = (Map) x.get(i);
				Map z = (Map) x.get(min_index);
				x.set(i,z);
				x.set(min_index,y);
			}

		}

		Map res = new HashMap();
		res.put("data",finalList);
		res.put("reason",downReason);
		res.put("total",reasonTotal);
		res.put("grandTotal",(int) GrandTotal);
		res.put("machineName",machineList);
		res.put("totalDuration",durationTotal);

//		this gloabl value assign in opportunity drill down graph for getting reaosns and reason wise total
		List downtime_reasons_only = new ArrayList();
		for (DowntimeReasonEntity d:downReason){
			downtime_reasons_only.add(d.getDowntime_reason());
		}

		availability_service.get_availability_reasons = downtime_reasons_only;


		availability_service.get_availability_reason_wise_total = reasonTotal;

		return res;
	}
}