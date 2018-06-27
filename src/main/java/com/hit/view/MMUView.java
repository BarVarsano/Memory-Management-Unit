package com.hit.view;

import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.TreeMap;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import com.hit.controller.MMUController;
import com.hit.memoryunits.Page;

/*
* Bar Varsano 307929497
* Amit Levy 305650517
*/
public class MMUView extends Observable implements View {
	/* Members */
	private Gui Ui;
	int ramCapacity = 1; // number of cols
	int numberOfPage = 5;
	int[] processToController;
	int ProcessAmount = 0;

	/* Constructor */
	public MMUView() {
		Ui = new Gui();
	}

	/* Public Methods */
	@Override
	public void start() {
		configPageTable();
		configProcessList();
		configPlayButtons();
		Ui.getFrame().setVisible(true);
	}

	public Gui getGui() {
		return Ui;
	}

	public void setPageColNumber(int number) {
		this.ramCapacity = number;
	}

	public void setNumberOfProcess(int numberOfProcess) {
		this.ProcessAmount = numberOfProcess;
	}

	public int[] getSelectedPageNumbers() {
		return this.processToController;
	}

	public void UpdateCounters(List<String> logFileAllLines) {
		Integer pageFaultCounter = 0;
		Integer pageReplacementCounter = 0;
		for (String currentLogFileLine : logFileAllLines) {
			if (currentLogFileLine.contains("PF")) {
				pageFaultCounter++;
			} else if (currentLogFileLine.contains("PR")) {
				pageReplacementCounter++;
			}
		}

		this.Ui.getPageFaultTextField().setText(pageFaultCounter.toString());
		this.Ui.getPageReplacementTextField().setText(pageReplacementCounter.toString());
	}

	public void UpdateTable(Map<Long, Page<byte[]>> ramPagesInTheEnd) {
		Map<Long, Page<byte[]>> sortedMapByKey = new TreeMap<>(ramPagesInTheEnd);
		Object[][] content = new Object[numberOfPage][ramCapacity];
		String[] pageId = new String[ramCapacity];
		int col = 0;

		for (java.lang.Long key : sortedMapByKey.keySet()) {
			updateCurrentColTable(content, col, sortedMapByKey.get(key));
			pageId[col] = "Page " + key;
			col++;
		}

		Ui.getTable().setModel(new DefaultTableModel(content, pageId));
	}

	/* Private Methods */
	@SuppressWarnings("unchecked")
	private void configProcessList() {
		String[] valuesa = new String[ProcessAmount];
		for (int i = 0; i < valuesa.length; i++) {
			valuesa[i] = "Process " + (i + 1);
		}
		Ui.getList().setModel(new AbstractListModel() {
			String[] values = valuesa;

			public int getSize() {
				return values.length;
			}

			public Object getElementAt(int index) {
				return values[index];
			}
		});
	}

	private void updateCurrentColTable(Object[][] tableContent, int currentCol, Page<byte[]> currentPage) {
		for (int i = 0; i < numberOfPage; ++i) {
			tableContent[i][currentCol] = currentPage.getContent()[i];
		}
	}

	private void notifyController() {
		setChanged();
		notifyObservers(this.processToController);
	}

	private void configPageTable() {
		Object[][] content = new Object[numberOfPage][ramCapacity];
		String[] pageId = new String[ramCapacity];
		for (int i = 0; i < numberOfPage; i++) {
			for (int j = 0; j < ramCapacity; j++) {
				content[i][j] = 0;
			}
		}

		for (int i = 0; i < pageId.length; i++) {
			pageId[i] = "page " + (i + 1);
		}
		Ui.getTable().setModel(new DefaultTableModel(content, pageId));
	}

	private void configPlayButtons() {
		Ui.getBtnPlayAll().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processToController = new int[ProcessAmount];
				for (int i = 0; i < processToController.length; ++i) {
					processToController[i] = i;
				}

				notifyController();
			}
		});

		Ui.getBtnPlay().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processToController = new int[Ui.getList().getSelectedValuesList().size()];
				int i = 0;
				for (Object currentSelectedValue : Ui.getList().getSelectedValuesList()) {
					processToController[i] = getProcessNumberFromString((String) currentSelectedValue);
					i++;
				}

				notifyController();
			}
		});
	}

	private int getProcessNumberFromString(String processDetail) {
		String[] SplittedBySpace = processDetail.split(" ");
		String[] SplittedByClosure = SplittedBySpace[1].split("]");
		return Integer.parseInt(SplittedByClosure[0]);
	}
}
