package com.benzrf.sblock.sburbplayers.commandparser;

import org.bukkit.entity.Player;

import com.benzrf.sblock.sburbplayers.commandparser.converters.*;
import com.benzrf.sblock.sburbplayers.commandparser.validators.*;

public enum ArgumentType
{
	PLAYER("player", new PlayerArgumentValidator(), new PlayerArgumentConverter(), Player.class),
	SPECIBUS("specibus", new SpecibusArgumentValidator(), new StringArgumentConverter(), String.class);
	
	private ArgumentType(String humanName, ArgumentValidator av, ArgumentConverter ac, Class<?> rt)
	{
		this.humanName = humanName;
		this.av = av;
		this.ac = ac;
		this.rt = rt;
	}
	
	public String getHumanName()
	{
		return this.humanName;
	}
	
	public boolean isArgumentValid(String arg)
	{
		return av.isArgumentValid(arg);
	}
	
	public Object convertArgument(String arg)
	{
		return ac.convertArgument(arg);
	}
	
	public Class<?> getReturnType()
	{
		return rt;
	}
	
	private String humanName;
	private ArgumentValidator av;
	private ArgumentConverter ac;
	private Class<?> rt;
}
