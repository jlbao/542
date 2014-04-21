package crawler;

import config.config.Config;
import crawler.tools.HttpOperation;

public class CompanyCrawler implements Runnable {

	@Override
	public void run() {
		while (Config.CurrentCompany != null) {
			while (Config.CurrentCompany.taskNum < Config.TaskNumPerCompany) { // if
																				// the
																				// company
																				// tasknum
																				// is
																				// not
																				// enough
				try {
					String taskList = HttpOperation
							.getPageContent(Config.CurrentCompany
									.getCurrentURL());
					// put into the tasks
					Config.CurrentCompany.putTasks(taskList);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// wait till the current company finished
			while (!Config.TaskQueue.isEmpty()) {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// need to switch Company
			Config.switchCompany();
		}
	}
}
