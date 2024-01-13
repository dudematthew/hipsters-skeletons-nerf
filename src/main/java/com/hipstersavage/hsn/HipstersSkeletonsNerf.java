package com.hipstersavage.hsn;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HipstersSkeletonsNerf implements ModInitializer {

	public static final String MOD_ID = "hsn";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		LOGGER.info("Hipster's Nerfed Skeletons initialized! Bye bye, OP Skellies!");
	}
}