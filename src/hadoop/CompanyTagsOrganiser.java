package hadoop;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.LineReader;

import crawler.adapter.CompanyWriter;
import crawler.adapter.FollowerReader;

public class CompanyTagsOrganiser {
	
	/*
	 * Mapper class. Reads one line in follower table, splits tags, outputs with a count 1
	 */
	public static class Map extends Mapper<LongWritable, Text, Text, Text> {

		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String companyName = value.toString().substring(0,
					value.toString().indexOf(","));
			String str = value.toString().substring(
					value.toString().indexOf(",") + 1);
			String[] tags = str.substring(str.indexOf(",") + 1).split(",");
			for (String tag : tags) {
				if (tag.length() > 0) {
					context.write(new Text(companyName), new Text(tag));
				}
			}
		}
	}

	/*
	 * Combiner class, locally counts tags of the same company, outputs the local total count of one tag
	 */
	public static class Combine extends Reducer<Text, Text, Text, Text> {
		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			for (Text text : values) {
				if (map.containsKey(text.toString())) {
					map.put(text.toString(), map.get(text.toString()) + 1);
				} else {
					map.put(text.toString(), 1);
				}
			}
			Iterator<Entry<String, Integer>> iter = map.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, Integer> ent = iter.next();
				context.write(key,
						new Text(ent.getKey() + "," + ent.getValue()));
			}
		}
	}
	
	/*
	 * Reducer class. Computes the global count for a tag of a company and uses PriorityQueue to output top 100 tags
	 */
	public static class Reduce extends Reducer<Text, Text, Text, Text> {
		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			for (Text text : values) {
				if (map.containsKey(text.toString())) {
					map.put(text.toString(),
							map.get(text.toString())
									+ Integer.parseInt(text.toString().split(
											",")[1]));
				} else {
					map.put(text.toString(),
							Integer.parseInt(text.toString().split(",")[1]));
				}
			}
			Comparator<Entry<String, Integer>> comp = new Comparator<Entry<String, Integer>>() {
				public int compare(Entry<String, Integer> entry1,
						Entry<String, Integer> entry2) {
					return entry1.getValue().compareTo(entry2.getValue());
				}
			};
			PriorityQueue<Entry<String, Integer>> queue = new PriorityQueue<Entry<String, Integer>>(
					1, comp);
			Iterator<Entry<String, Integer>> iter = map.entrySet().iterator();
			while (iter.hasNext()) {
				if (queue.size() < map.size() / 100) {
					queue.add(iter.next());
				} else {
					Entry<String, Integer> e = queue.peek();
					Entry<String, Integer> entry = iter.next();
					if (entry.getValue().compareTo(e.getValue()) > 0) {
						queue.poll();
						queue.add(entry);
					}
				}
			}
			Entry<String, Integer> entry;
			String str = new String();
			while ((entry = queue.peek()) != null) {
				str += entry.getKey().substring(0, entry.getKey().indexOf(","));
				if (queue.size() > 1) {
					str += ",";
				}
				queue.poll();
			}
			context.write(key, new Text(str));
		}
	}

	/*
	 * Main method. Creates jobs, loads data from MySQL, run jobs and outputs to local files and MySQL
	 * For tests, we set a upperbound of rounds the program will run (currently 10)
	 */
	public static void main(String[] args) throws Exception {
		int count = 1;
		long longest = Long.MIN_VALUE;
		long shortest = Long.MAX_VALUE;
		long average = 0;
		while (count < 11) {
			long startTime = System.currentTimeMillis();

			Configuration conf = new Configuration();

			FollowerReader fReader = new FollowerReader();
			String rOutput = fReader.run();

			FileSystem fs = FileSystem.get(conf);
			Path inputPath = new Path("test" + count + "//input");
			Path outputPath = new Path("test" + count + "//output");

			fs.copyFromLocalFile(new Path(rOutput), inputPath);

			Job job = new Job(conf, "CompanyTagsOrganiser");
			job.setJarByClass(CompanyTagsOrganiser.class);
			job.setNumReduceTasks(1);

			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(Text.class);
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(Text.class);

			job.setMapperClass(Map.class);
			job.setCombinerClass(Combine.class);
			job.setReducerClass(Reduce.class);

			job.setInputFormatClass(TextInputFormat.class);
			job.setOutputFormatClass(TextOutputFormat.class);

			FileInputFormat.setInputPaths(job, inputPath);
			FileOutputFormat.setOutputPath(job, outputPath);

			job.waitForCompletion(true);

			fs = FileSystem.get(conf);
			FSDataInputStream dis = fs.open(new Path(outputPath
					+ "/part-r-00000"));
			LineReader lr = new LineReader(dis, conf);
			String fileName = "companyTags.txt";
			File file = new File(fileName);
			FileWriter fw = new FileWriter(file, false);
			BufferedWriter bw = new BufferedWriter(fw);
			Text str = new Text();
			while (lr.readLine(str) > 0) {
				bw.write(str.toString() + '\n');
			}
			bw.close();
			fw.close();
			dis.close();
			fs.close();

			CompanyWriter cw = new CompanyWriter(fileName);
			cw.run();

			long endTime = System.currentTimeMillis();

			long duration = endTime - startTime;
			if (duration > longest) {
				longest = duration;
			}

			if (duration < shortest) {
				shortest = duration;
			}
			average += duration;

			System.out.println(duration + " ms elapsed");

			count++;
			Thread.sleep(10000);
		}

		average /= 10;

		System.out.println("Longest: " + longest + " ms");
		System.out.println("Shortest: " + shortest + " ms");
		System.out.println("Average: " + average + " ms");

	}
}