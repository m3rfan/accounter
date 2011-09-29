/**
 * 
 */
package com.vimukti.accounter.mobile.store;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.vimukti.accounter.mobile.AccounterMobileException;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.PatternResult;
import com.vimukti.accounter.mobile.Result;

/**
 * @author Prasanna Kumar G
 * 
 */
public class PatternStore {
	Logger log = Logger.getLogger(PatternStore.class);

	public static PatternStore INSTANCE = new PatternStore();

	private Map<String, Result> patterns = new HashMap<String, Result>();

	public Result find(String pattern) {
		return patterns.get(pattern.toLowerCase().trim());
	}

	public void reload() throws AccounterMobileException {
		try {
			log.info("Loading Patterns...");
			XStream xStream = getPatternXStream();

			File file = getFile("");

			List<Object> objects = (List<Object>) xStream
					.fromXML(new FileInputStream(file));

			updateMap(objects);

		} catch (Exception e) {
			e.printStackTrace();
			throw new AccounterMobileException(e);
		}
	}

	private XStream getPatternXStream() {
		XStream xStream = new XStream(new DomDriver());

		xStream.alias("patterns", List.class);
		xStream.alias("include", String.class);

		xStream.alias("pattern", Pattern.class);
		xStream.alias("input", String.class);

		xStream.addImplicitCollection(Pattern.class, "inputs");

		xStream.alias("output", List.class);

		xStream.alias("text", String.class);
		xStream.alias("command", String.class);

		return xStream;

	}

	/**
	 * @param objects
	 * @throws FileNotFoundException
	 */
	private void updateMap(List<Object> objects) throws FileNotFoundException {
		for (Object obj : objects) {
			if (obj instanceof String) {
				XStream xStream = getPatternXStream();
				File include = new File(
						"./src/com/vimukti/accounter/mobile/store/"
								+ (String) obj);
				List<Object> fromXML = (List<Object>) xStream
						.fromXML(new FileInputStream(include));
				updateMap(fromXML);
			} else if (obj instanceof Pattern) {
				Pattern pattern = (Pattern) obj;
				PatternResult result = new PatternResult();
				CommandList commands = new CommandList();
				if (pattern.output != null && !pattern.output.isEmpty()) {
					String message = pattern.output.get(0);
					result.setTitle(message);
					pattern.output.remove(message);
					commands.addAll(pattern.output);
				}
				result.setCommands(commands);
				if (pattern.inputs != null) {
					for (String input : pattern.inputs) {
						patterns.put(input.toLowerCase(), result);
					}
				}
			}
		}
	}

	/**
	 * @param string
	 * @return
	 */
	private File getFile(String language) {
		return new File("./src/com/vimukti/accounter/mobile/store/patterns.xml");
	}

	private class Pattern {
		List<String> inputs;
		List<String> output;
	}

}
