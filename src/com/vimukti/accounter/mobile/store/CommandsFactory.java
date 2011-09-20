/**
 * 
 */
package com.vimukti.accounter.mobile.store;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.vimukti.accounter.mobile.AccounterMobileException;
import com.vimukti.accounter.mobile.Command;

/**
 * @author Prasanna Kumar G
 * 
 */
public class CommandsFactory {

	Logger log = Logger.getLogger(CommandsFactory.class);

	private Map<String, Class<?>> commands = new HashMap<String, Class<?>>();

	public static CommandsFactory INSTANCE = new CommandsFactory();

	public void reload() throws AccounterMobileException {
		loadCommands();
	}

	/**
	 * Search for the Command in the CommandFactory and Returns the Command if
	 * Exists
	 * 
	 * @param command
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public Command searchCommand(String commandString) {
		try {
			// TODO use RegularExpression
			Class<?> className = commands.get(commandString.toLowerCase());
			if (className == null) {
				return null;
			}
			Command command = (Command) className.newInstance();
			command.setName(commandString);
			return command;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Command getCommand(String commandName) {
		try {
			Class<?> clazz = commands.get(commandName);
			return (Command) clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * Loads All Commands
	 * 
	 * @throws AccounterMobileException
	 */
	private void loadCommands() throws AccounterMobileException {
		try {
			log.info("Loading Commands...");
			XStream xStream = new XStream(new DomDriver());

			xStream.alias("commands", List.class);

			xStream.alias("command", CommandTemplate.class);
			xStream.useAttributeFor(CommandTemplate.class, "name");
			xStream.useAttributeFor(CommandTemplate.class, "className");
			xStream.aliasField("clazz", CommandTemplate.class, "className");

			xStream.alias("alias", String.class);
			xStream.addImplicitCollection(CommandTemplate.class, "aliases");

			File file = getFile("");

			Object object = xStream.fromXML(new FileInputStream(file));

			List<CommandTemplate> commands = (List<CommandTemplate>) object;
			for (CommandTemplate command : commands) {
				Class<?> forName = Class
						.forName("com.vimukti.accounter.mobile.commands."
								+ command.className);
				this.commands.put(command.name.toLowerCase(), forName);
				for (String alias : command.aliases) {
					this.commands.put(alias.toLowerCase(), forName);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new AccounterMobileException(
					AccounterMobileException.ERROR_INTERNAL, e);
		}
	}

	/**
	 * @param string
	 * @return
	 */
	private File getFile(String language) {
		return new File("./src/com/vimukti/accounter/mobile/store/commands.xml");
	}

	class CommandTemplate {
		String name;
		String className;
		List<String> aliases;
	}

}
