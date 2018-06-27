package com.hit.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.hit.driver.CLI;
import com.hit.model.MMUModel;
import com.hit.model.Model;
import com.hit.view.MMUView;
import com.hit.view.View;

/*
* Bar Varsano 307929497
* Amit Levy 305650517
*/
public class MMUController implements Controller, Observer {
	/* Members */
	private MMUView view;
	private MMUModel model;

	/* Constructor */
	public MMUController(Model model, View view) {
		this.view = (MMUView) view;
		this.model = (MMUModel) model;
	}

	/* Public Methods */
	public int getRamCapacity() {
		return model.getRamCapacity();
	}
	
	@Override
	public void update(Observable o, Object arg) {

		if (o instanceof CLI)
		{
			((MMUView) this.view).setPageColNumber(Integer.parseInt(arg.toString().split(" ")[1]));
			((MMUModel) this.model).setConfiguration(Arrays.asList(arg.toString().split(" ")));
			((MMUModel) this.model).start();
			((MMUView) this.view).setNumberOfProcess(((MMUModel) this.model).getNumProcesses());	
			((MMUView) this.view).start();		
		}
		else if (o instanceof MMUModel)
		{		
			((MMUView) this.view).UpdateCounters((List<String>) arg);
			((MMUView) this.view).UpdateTable(((MMUModel) this.model).getRamPagesInTheEnd());
		}
		else if (o instanceof MMUView)
		{

			List<com.hit.processes.Process> actualProcessesToRun = new LinkedList<>();
			for (com.hit.processes.Process currentCheckedProcess : ((MMUModel) this.model).getProcesses())
			{
				for (int i = 0; i < ((int[]) arg).length; i++)
				{
					if (currentCheckedProcess.getId() == ((int[]) arg)[i])
					{
						actualProcessesToRun.add(currentCheckedProcess);
						break;
					}
				}
			}
			
			((MMUModel) this.model).startProcessProgress(actualProcessesToRun);
		}
	}
}


