package crawler;

import config.config.Config;
import config.config.Task;
import crawler.tools.HttpOperation;
import crawler.tools.Parser;

public class TaskCrawler implements Runnable {

	/*
	 * crawl the tags for each user, and put them into the store queue
	 */
	@Override
	public void run() {
		while (true) {
			Task task = Config.TaskQueue.poll();
			if (task != null) {
				try {
					String content = HttpOperation.getPageContent(task.taskURL);
					task.tags = Parser.getTags(content);
					logs(task);
					Config.StoreQueue.add(task);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	// logs
	public void logs(Task task) {
		if (task.userID.isEmpty()) {
			System.out.println("no userID");
		} else {
			System.out.print(task.userID + " ");
			System.out.print(task.tags);
			System.out.println();
		}
	}
}
