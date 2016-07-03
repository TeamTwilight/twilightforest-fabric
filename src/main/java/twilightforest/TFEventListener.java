package twilightforest;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandGameRule;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.event.world.WorldEvent;
import twilightforest.biomes.TFBiomeBase;
import twilightforest.block.TFBlocks;
import twilightforest.enchantment.TFEnchantment;
import twilightforest.entity.EntityTFCharmEffect;
import twilightforest.entity.EntityTFPinchBeetle;
import twilightforest.entity.EntityTFYeti;
import twilightforest.item.TFItems;
import twilightforest.world.ChunkProviderTwilightForest;
import twilightforest.world.TFWorldChunkManager;
import twilightforest.world.WorldProviderTwilightForest;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * So much of the mod logic in this one class
 */
public class TFEventListener {
	
	protected HashMap<String, InventoryPlayer> playerKeepsMap = new HashMap<String, InventoryPlayer>();
	private boolean isBreakingWithGiantPick = false;
	private boolean shouldMakeGiantCobble = false;
	private int amountOfCobbleToReplace = 0;
	private long lastSpawnedHintMonsterTime;

	/**
	 * Check if the player picks up a lich scepter, and if so, check for the scepter mastery achievement
	 */
	@SubscribeEvent
	public void pickupItem(EntityItemPickupEvent event) {
		Item item = event.getItem().getEntityItem().getItem();
		if (item == TFItems.scepterTwilight || item == TFItems.scepterLifeDrain
				|| item == TFItems.scepterZombie) {
			// the player has picked up a scepter.  Check if they have them all in their inventory, and if so, achievement
			//System.out.println("Player picked up a scepter");
			checkPlayerForScepterMastery(event.getEntityPlayer());
			event.getEntityPlayer().addStat(TFAchievementPage.twilightProgressLich);
		}
		
		// naga scale gives naga progress achievement
		if (item == TFItems.nagaScale) {
			event.getEntityPlayer().addStat(TFAchievementPage.twilightProgressNaga);
		}
		// trophy gives kill achievement
		if (item == TFItems.trophy && event.getItem().getEntityItem().getItemDamage() == 0) {
			event.getEntityPlayer().addStat(TFAchievementPage.twilightKillHydra);
		}
		if (item == TFItems.trophy && event.getItem().getEntityItem().getItemDamage() == 1) {
			event.getEntityPlayer().addStat(TFAchievementPage.twilightKillNaga);
		}
		if (item == TFItems.trophy && event.getItem().getEntityItem().getItemDamage() == 2) {
			event.getEntityPlayer().addStat(TFAchievementPage.twilightKillLich);
		}
		if (item == TFItems.trophy && event.getItem().getEntityItem().getItemDamage() == 3) {
			event.getEntityPlayer().addStat(TFAchievementPage.twilightProgressUrghast);
		}		
		if (item == TFItems.trophy && event.getItem().getEntityItem().getItemDamage() == 4) {
			event.getEntityPlayer().addStat(TFAchievementPage.twilightProgressGlacier);
		}
		// mazebreaker
		if (item == TFItems.mazebreakerPick) {
			event.getEntityPlayer().addStat(TFAchievementPage.twilightMazebreaker);
		}
		// meef stroganoff (or axe)
		if (item == TFItems.meefStroganoff || item == TFItems.minotaurAxe) {
			event.getEntityPlayer().addStat(TFAchievementPage.twilightProgressLabyrinth);
		}
		// fiery blood
		if (item == TFItems.fieryBlood) {
			event.getEntityPlayer().addStat(TFAchievementPage.twilightProgressHydra);
		}
		// phantom helm/plate
		if (item == TFItems.phantomHelm || item == TFItems.phantomPlate) {
			event.getEntityPlayer().addStat(TFAchievementPage.twilightProgressKnights);
		}
		// fiery tears
		if (item == TFItems.fieryTears) {
			event.getEntityPlayer().addStat(TFAchievementPage.twilightProgressUrghast);
		}
		// yeti items
		if (item == TFItems.alphaFur || item == TFItems.yetiBoots || item == TFItems.yetiHelm
				|| item == TFItems.yetiPlate || item == TFItems.yetiLegs) {
			event.getEntityPlayer().addStat(TFAchievementPage.twilightProgressYeti);
		}
		// lamp of cinders
		if (item == TFItems.lampOfCinders) {
			event.getEntityPlayer().addStat(TFAchievementPage.twilightProgressTroll);
		}
	}

	/**
	 * Does the player have all three scepters somewhere in the inventory?
	 * @param player
	 */
	private void checkPlayerForScepterMastery(EntityPlayer player)
	{
		boolean scepterTwilight = false;
		boolean scepterLifeDrain = false;
		boolean scepterZombie = false;
		
		IInventory inv = player.inventory;
		
		for (int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack stack = inv.getStackInSlot(i);
			
			if (stack != null && stack.getItem() == TFItems.scepterTwilight)
			{
				scepterTwilight = true;
			}
			if (stack != null && stack.getItem() == TFItems.scepterLifeDrain)
			{
				scepterLifeDrain = true;
			}
			if (stack != null && stack.getItem() == TFItems.scepterZombie)
			{
				scepterZombie = true;
			}
		}
		
		if (scepterTwilight && scepterLifeDrain && scepterZombie)
		{
			player.addStat(TFAchievementPage.twilightLichScepters);
		}
	}

	/**
	 * Check if we've crafted something that there's an achievement for
	 */
	@SubscribeEvent
	public void onCrafting(ItemCraftedEvent event) {
		
		//System.out.println("Getting item crafted event");
		
		ItemStack itemStack = event.crafting;
		EntityPlayer player = event.player;
		
		// if the item is naga armor
    	if ((itemStack.getItem() == TFItems.plateNaga || itemStack.getItem() == TFItems.legsNaga)) {
    		// check if the player has made both armors
    		checkPlayerForNagaArmorer(player);
    	}
    	
    	// trigger achievements
    	if (itemStack.getItem() == TFItems.magicMapFocus) {
    		player.addStat(TFAchievementPage.twilightMagicMapFocus);
    	}
    	if (itemStack.getItem() == TFItems.emptyMagicMap) {
    		player.addStat(TFAchievementPage.twilightMagicMap);
    	}
    	if (itemStack.getItem() == TFItems.emptyMazeMap) {
    		player.addStat(TFAchievementPage.twilightMazeMap);
    	}
    	if (itemStack.getItem() == TFItems.emptyOreMap) {
    		player.addStat(TFAchievementPage.twilightOreMap);
    	}
    	
    	// if we've crafted 64 planks from a giant log, sneak 192 more planks into the player's inventory or drop them nearby
    	if (itemStack.getItem() == Item.getItemFromBlock(Blocks.PLANKS) && itemStack.stackSize == 64 && this.doesCraftMatrixHaveGiantLog(event.craftMatrix)) {
    		addToPlayerInventoryOrDrop(player, new ItemStack(Blocks.PLANKS, 64));
    		addToPlayerInventoryOrDrop(player, new ItemStack(Blocks.PLANKS, 64));
    		addToPlayerInventoryOrDrop(player, new ItemStack(Blocks.PLANKS, 64));

    	}
	}

	private void addToPlayerInventoryOrDrop(EntityPlayer player, ItemStack planks) {
		if (!player.inventory.addItemStackToInventory(planks)) {
			player.dropItem(planks, false);
		}
	}

	private boolean doesCraftMatrixHaveGiantLog(IInventory inv) {
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			
			if (stack != null && stack.getItem() == Item.getItemFromBlock(TFBlocks.giantLog)) {
				return true;
			}
		}
		
		
		return false;
	}

	/**
	 * Does the player have two naga armors?
	 */
	private void checkPlayerForNagaArmorer(EntityPlayer player)
	{
		boolean nagaScale = false;
		boolean legsNaga = false;
		
		IInventory inv = player.inventory;
		
		for (int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack stack = inv.getStackInSlot(i);
			
			if (stack != null && stack.getItem() == TFItems.nagaScale)
			{
				nagaScale = true;
			}
			if (stack != null && stack.getItem() == TFItems.legsNaga)
			{
				legsNaga = true;
			}
		}
		
		if (nagaScale && legsNaga)
		{
			player.addStat(TFAchievementPage.twilightNagaArmors);
		}
	}
	
	/**
	 * Check to see if a smeltable block has dropped with a fiery tool, and if so, smelt it
	 * Also check if we need to transform 64 cobbles into a giant cobble
	 */
	@SubscribeEvent
	public void harvestDrops(HarvestDropsEvent event) {
		if (event.getHarvester() != null && event.getHarvester().inventory.getCurrentItem() != null && event.getHarvester().inventory.getCurrentItem().getItem().func_150897_b(event.block)) {
			if (event.getHarvester().inventory.getCurrentItem().getItem() == TFItems.fieryPick) {
				ArrayList<ItemStack> removeThese = new ArrayList<ItemStack>(1);
				ArrayList<ItemStack> addThese = new ArrayList<ItemStack>(1);

				for (ItemStack input : event.getDrops())
				{
					// does it smelt?
					ItemStack result = FurnaceRecipes.instance().getSmeltingResult(input);
					if (result != null)
					{
						addThese.add(new ItemStack(result.getItem(), input.stackSize));
						removeThese.add(input);

						// spawn XP
						spawnSpeltXP(result, event.getWorld(), event.x, event.y, event.z);
					}
				}

				// remove things we've decided to remove
				event.getDrops().removeAll(removeThese);
				
				// add things we add
				event.getDrops().addAll(addThese);
			} 
		} 
		
		// this flag is set in reaction to the breakBlock event, but we need to remove the drops in this event
		if (this.shouldMakeGiantCobble && event.getDrops().size() > 0) {
			// turn the next 64 cobblestone drops into one giant cobble
			if (event.getDrops().get(0).getItem() == Item.getItemFromBlock(Blocks.COBBLESTONE)) {
				event.getDrops().remove(0);
				if (this.amountOfCobbleToReplace == 64) {
					event.getDrops().add(new ItemStack(TFBlocks.giantCobble));
				}
				
				this.amountOfCobbleToReplace--;
				
				if (this.amountOfCobbleToReplace <= 0) {
					this.shouldMakeGiantCobble = false;
				}
			}
		}
	}

	/**
	 * Spawn XP for smelting the specified item at the specified location
	 */
	private void spawnSpeltXP(ItemStack smelted, World world, int x, int y, int z) {
		float floatXP = FurnaceRecipes.instance().getSmeltingExperience(smelted);
		int smeltXP = (int)floatXP;
		// random chance of +1 XP to handle fractions
		if (floatXP > smeltXP && world.rand.nextFloat() < (floatXP - smeltXP))
		{
			smeltXP++;
		}

		while (smeltXP > 0)
		{
			int splitXP = EntityXPOrb.getXPSplit(smeltXP);
			smeltXP -= splitXP;
			world.spawnEntityInWorld(new EntityXPOrb(world, x + 0.5D, y + 0.5D, z + 0.5D, splitXP));
		}
	}

	/**
	 * We wait for a player wearing armor with the fire react enchantment to get hurt, and if that happens, we react
	 */
	@SubscribeEvent
	public void entityHurts(LivingHurtEvent event) 
	{
		// fire aura
		if (event.getEntityLiving() instanceof EntityPlayer && event.getSource().damageType.equals("mob") && event.getSource().getEntity() != null)
		{
			EntityPlayer player = (EntityPlayer)event.getEntityLiving();
			int fireLevel = TFEnchantment.getFieryAuraLevel(player.inventory, event.getSource());
			
			//System.out.println("Detecting a fire reaction event.  Reaction level is " + fireLevel);
			
			if (fireLevel > 0 && player.getRNG().nextInt(25) < fireLevel)
			{
				//System.out.println("Executing fire reaction.");
				event.getSource().getEntity().setFire(fireLevel / 2);
			}
		}
		
		// chill aura
		if (event.getEntityLiving() instanceof EntityPlayer && event.getSource().damageType.equals("mob") 
				&& event.getSource().getEntity() != null && event.getSource().getEntity() instanceof EntityLivingBase) {
			EntityPlayer player = (EntityPlayer)event.getEntityLiving();
			int chillLevel = TFEnchantment.getChillAuraLevel(player.inventory, event.getSource());
			
			//System.out.println("Detecting a chill aura event.  Reaction level is " + chillLevel);
			
			if (chillLevel > 0) {
				//System.out.println("Executing chill reaction.");
				((EntityLivingBase)event.getSource().getEntity()).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS.id, chillLevel * 5 + 5, chillLevel));

			}
		}
		
		// triple bow strips hurtResistantTime
		if (event.getSource().damageType.equals("arrow") && event.getSource().getEntity() != null && event.getSource().getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)event.getSource().getEntity();
			
			if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == TFItems.tripleBow
					|| player.getHeldItemOffhand() != null && player.getHeldItemOffhand().getItem() == TFItems.tripleBow) {
				//System.out.println("Triplebow Arrows!");
				event.getEntityLiving().hurtResistantTime = 0;
			}
		}
		
		// ice bow freezes
		if (event.getSource().damageType.equals("arrow") && event.getSource().getEntity() != null && event.getSource().getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)event.getSource().getEntity();

			if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == TFItems.iceBow
					|| player.getHeldItemOffhand() != null && player.getHeldItemOffhand().getItem() == TFItems.iceBow) {

				int chillLevel = 2;
				event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 20 * 10, chillLevel, true, true));
			}
		}
		
		// enderbow teleports
		if (event.getSource().damageType.equals("arrow") && event.getSource().getEntity() != null && event.getSource().getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)event.getSource().getEntity();
			
			if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == TFItems.enderBow) {
				
				double sourceX = player.posX;
				double sourceY = player.posY;
				double sourceZ = player.posZ;
				float sourceYaw = player.rotationYaw;
				float sourcePitch = player.rotationPitch;
				
				// this is the only method that will move the player properly
				player.rotationYaw = event.getEntityLiving().rotationYaw;
				player.rotationPitch = event.getEntityLiving().rotationPitch;
				player.setPositionAndUpdate(event.getEntityLiving().posX, event.getEntityLiving().posY, event.getEntityLiving().posZ);
				player.playSound("mob.endermen.portal", 1.0F, 1.0F);

				
				// monsters are easy to move
				event.getEntityLiving().setPositionAndRotation(sourceX, sourceY, sourceZ, sourceYaw, sourcePitch);
				event.getEntityLiving().playSound("mob.endermen.portal", 1.0F, 1.0F);
				 

				//System.out.println("Enderbow Arrow!");
			}
		}
		
		// charm of life?
		if (event.getEntityLiving() instanceof EntityPlayer && willEntityDie(event))
		{
			EntityPlayer player = (EntityPlayer)event.getEntityLiving();
			
			boolean charm1 = false;
			boolean charm2 = player.inventory.consumeInventoryItem(TFItems.charmOfLife2);
			if (!charm2)
			{
				charm1 = player.inventory.consumeInventoryItem(TFItems.charmOfLife1);
			}
			
			// do they have a charm of life?  OM NOM NOM!
			if (charm2 || charm1)
			{
				//player.addChatMessage("Charm of Life saves you!!!");

				// cancel damage
				event.setResult(Result.DENY);
				event.setCanceled(true);
				event.setAmount(0);

				if (charm1)
				{
					player.setHealth(8);
					player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, 0));
				}
				
				if (charm2)
				{
					player.setHealth((float) player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue());
					
					player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 600, 3));
					player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 600, 0));
					player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 600, 0));
				}
				
				// spawn effect thingers
				EntityTFCharmEffect effect = new EntityTFCharmEffect(player.worldObj, player, charm1 ? TFItems.charmOfLife1 :  TFItems.charmOfLife2);
				player.worldObj.spawnEntityInWorld(effect);
				
				EntityTFCharmEffect effect2 = new EntityTFCharmEffect(player.worldObj, player, charm1 ? TFItems.charmOfLife1 :  TFItems.charmOfLife2);
				effect2.offset = (float) Math.PI;
				player.worldObj.spawnEntityInWorld(effect2);
				
				// sound
				player.worldObj.playSoundEffect(player.posX + 0.5D, player.posY + 0.5D, player.posZ + 0.5D, "mob.zombie.unfect", 1.5F, 1.0F);

			}
		}
	}

	/**
	 * Is this damage likely to kill the target?
	 */
	public boolean willEntityDie(LivingHurtEvent event) 
	{
		float amount = event.getAmount();
		DamageSource source = event.getSource();
		EntityLivingBase living = event.getEntityLiving();
		// reduce damage by armor
        if (!source.isUnblockable()){
            int armor = 25 - living.getTotalArmorValue();
            //System.out.println("Initial amount = " + amount + ", armor = " + armor + " so damage after armor is " + ((amount * armor) / 25F));
            amount = (amount * armor) / 25F;
        }

		// maybe also potions?
        if (living.isPotionActive(MobEffects.RESISTANCE)) {
            int resistance = 25 - (living.getActivePotionEffect(MobEffects.RESISTANCE).getAmplifier() + 1) * 5;
            amount = amount * resistance / 25F;
        }
        //System.out.printf("I think the player is going to take %f damage and they have %f health.\n", Math.ceil(amount), living.getHealth());
        
		return Math.ceil(amount) >= Math.floor(living.getHealth());
	}
	
	/**
	 * Catch bonemeal use
	 */
	@SubscribeEvent
	public void bonemealUsed(BonemealEvent event)
	{
		if (event.getBlock().getBlock() == TFBlocks.sapling)
		{
            if (!event.getWorld().isRemote)
            {
                ((BlockSapling)TFBlocks.sapling).grow(event.getWorld(), event.getPos(), event.getBlock(), event.getWorld().rand);

                event.setResult(Result.ALLOW);
            }
		}
	}
	
	
	/**
	 * If a player dies with a charm of keeping, consume the charm and then keep track of what items we need to keep
	 * 
	 * Also keep tower keys
	 */
	@SubscribeEvent
	public void livingDies(LivingDeathEvent event)
	{
		if (event.getEntityLiving() instanceof EntityPlayer && !event.getEntityLiving().worldObj.getGameRules().getGameRuleBooleanValue("keepInventory"))
		{
			EntityPlayer player = (EntityPlayer)event.getEntityLiving();
			
			if (player.inventory.consumeInventoryItem(TFItems.charmOfKeeping3))
			{
				FMLLog.info("[TwilightForest] Player died with charm of keeping III!  Keep it all!");
				InventoryPlayer keepInventory = new InventoryPlayer(null);
				
				// armor and full inventory
				keepAllArmor(player, keepInventory);
				for (int i = 0; i < player.inventory.mainInventory.length; i++)
				{
					keepInventory.mainInventory[i] = ItemStack.copyItemStack(player.inventory.mainInventory[i]);
					player.inventory.mainInventory[i] = null;
				}
				keepInventory.setItemStack(new ItemStack(TFItems.charmOfKeeping3));

				playerKeepsMap.put(player.getName(), keepInventory);
			}
			else if (player.inventory.consumeInventoryItem(TFItems.charmOfKeeping2))
			{
				FMLLog.info("[TwilightForest] Player died with charm of keeping II!  Keep armor and hotbar!");
				InventoryPlayer keepInventory = new InventoryPlayer(null);
				
				keepAllArmor(player, keepInventory);
				for (int i = 0; i < 9; i++)
				{
					keepInventory.mainInventory[i] = ItemStack.copyItemStack(player.inventory.mainInventory[i]);
					player.inventory.mainInventory[i] = null;
				}
				keepInventory.setItemStack(new ItemStack(TFItems.charmOfKeeping2));

				playerKeepsMap.put(player.getName(), keepInventory);
			}
			else if (player.inventory.consumeInventoryItem(TFItems.charmOfKeeping1))
			{
				FMLLog.info("[TwilightForest] Player died with charm of keeping I!  Keep armor and current item!");
				InventoryPlayer keepInventory = new InventoryPlayer(null);
				
				keepAllArmor(player, keepInventory);
				if (player.inventory.getCurrentItem() != null)
				{
					keepInventory.mainInventory[player.inventory.currentItem] = ItemStack.copyItemStack(player.inventory.mainInventory[player.inventory.currentItem]);
					player.inventory.mainInventory[player.inventory.currentItem] = null;
				}
				keepInventory.setItemStack(new ItemStack(TFItems.charmOfKeeping1));

				playerKeepsMap.put(player.getName(), keepInventory);
			}
			
			// check for tower keys
			if (player.inventory.hasItem(TFItems.towerKey))
			{
				InventoryPlayer keepInventory = retrieveOrMakeKeepInventory(player);
				// keep them all
				for (int i = 0; i < player.inventory.mainInventory.length; i++)
				{
					if (player.inventory.mainInventory[i] != null && player.inventory.mainInventory[i].getItem() == TFItems.towerKey)
					{
						keepInventory.mainInventory[i] = ItemStack.copyItemStack(player.inventory.mainInventory[i]);
						player.inventory.mainInventory[i] = null;
					}
				}
				playerKeepsMap.put(player.getName(), keepInventory);
			}
		}
		
		if (playerKeepsMap.size() > 1)
		{
			FMLLog.warning("[TwilightForest] Twilight Forest mod is keeping track of a lot of dead player inventories.  Has there been an apocalypse?");
		}
	}

	/**
	 * If we have a stored inventory already, return that, if not, make a new one.
	 */
	private InventoryPlayer retrieveOrMakeKeepInventory(EntityPlayer player) 
	{
		if (playerKeepsMap.containsKey(player.getName()))
		{
			return playerKeepsMap.get(player.getName());
		}
		else
		{
			return new InventoryPlayer(null);
		}
	}

	/**
	 * Move the full armor inventory to the keep pile
	 */
	private void keepAllArmor(EntityPlayer player, InventoryPlayer keepInventory) {
		for (int i = 0; i < player.inventory.armorInventory.length; i++)
		{
			keepInventory.armorInventory[i] = ItemStack.copyItemStack(player.inventory.armorInventory[i]);
			player.inventory.armorInventory[i] = null;
		}
	}
	/**
	 * Maybe we kept some stuff for the player!
	 */
	@SubscribeEvent
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		EntityPlayer player = event.player;
		if (playerKeepsMap.containsKey(player.getName()))
		{
			FMLLog.info("[TwilightForest] Player %s respawned and recieved items held in storage", player.getName());
			
			InventoryPlayer keepInventory = playerKeepsMap.get(player.getName());
			
			for (int i = 0; i < player.inventory.armorInventory.length; i++)
			{
				if (keepInventory.armorInventory[i] != null)
				{
					player.inventory.armorInventory[i] = keepInventory.armorInventory[i];
				}
			}
			for (int i = 0; i < player.inventory.mainInventory.length; i++)
			{
				if (keepInventory.mainInventory[i] != null)
				{
					player.inventory.mainInventory[i] = keepInventory.mainInventory[i];
				}
			}
			
			// spawn effect thingers
			if (keepInventory.getItemStack() != null)
			{
				EntityTFCharmEffect effect = new EntityTFCharmEffect(player.worldObj, player, keepInventory.getItemStack().getItem());
				player.worldObj.spawnEntityInWorld(effect);
				
				EntityTFCharmEffect effect2 = new EntityTFCharmEffect(player.worldObj, player, keepInventory.getItemStack().getItem());
				effect2.offset = (float) Math.PI;
				player.worldObj.spawnEntityInWorld(effect2);
	
				player.worldObj.playSoundEffect(player.posX + 0.5D, player.posY + 0.5D, player.posZ + 0.5D, "mob.zombie.unfect", 1.5F, 1.0F);
			}

			playerKeepsMap.remove(player.getName());
		}
	}

	/**
	 * Dump stored items if player logs out
	 */
	@SubscribeEvent
	public void onPlayerLogout(PlayerLoggedOutEvent event) { 
		EntityPlayer player = event.player;
		if (playerKeepsMap.containsKey(player.getName()))
		{
			FMLLog.warning("[TwilightForest] Mod was keeping inventory items in reserve for player %s but they logged out!  Items are being dropped.", player.getName());
			InventoryPlayer keepInventory = playerKeepsMap.get(player.getName());
			
			// set player to the player logging out
			keepInventory.player = player;
			keepInventory.dropAllItems();
			
			playerKeepsMap.remove(player.getName());
		}
	}
	
	/**
	 * Stop the game from rendering the mount health for unfriendly creatures
	 */
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public boolean preOverlay(RenderGameOverlayEvent.Pre event)
	{
		if (event.getType() == RenderGameOverlayEvent.ElementType.HEALTHMOUNT)
		{
			if (isRidingUnfriendly(Minecraft.getMinecraft().thePlayer))
			{
				event.setCanceled(true);
				return false;

			}
		}
		return true;
	}
	
	
	/**
	 * Stop the player from sneaking while riding an unfriendly creature
	 */
	@SubscribeEvent
	public boolean livingUpdate(LivingUpdateEvent event)
	{
		if (event.getEntityLiving() instanceof EntityPlayer && event.getEntityLiving().isSneaking() && isRidingUnfriendly(event.getEntityLiving()))
		{
			event.getEntityLiving().setSneaking(false);
		}
		return true;
	}

	/**
	 * Is the specified entity riding an unfriendly creature (pinch beetle?)
	 */
	private boolean isRidingUnfriendly(EntityLivingBase entity) {
		return entity.isRiding() && (entity.getRidingEntity() instanceof EntityTFPinchBeetle || entity.getRidingEntity() instanceof EntityTFYeti);
	}
	
	/**
	 * Check if the player is trying to break a block in a structure that's considered unbreakable for progression reasons
	 * Also check for breaking blocks with the giant's pickaxe and maybe break nearby blocks
	 */
	@SubscribeEvent
	public void breakBlock(BreakEvent event) {
		if (!event.getPlayer().capabilities.isCreativeMode && isAreaProtected(event.getWorld(), event.getPlayer(), event.x, event.y, event.z) && isBlockProtectedFromBreaking(event.getWorld(), event.x, event.y, event.z)) {
			event.setCanceled(true);
		} else if (!this.isBreakingWithGiantPick && event.getPlayer().getCurrentEquippedItem() != null && event.getPlayer().getCurrentEquippedItem().getItem() == TFItems.giantPick && event.getPlayer().getCurrentEquippedItem().getItem().func_150897_b(event.block)) {
			//System.out.println("Breaking with giant pick!");
			
			this.isBreakingWithGiantPick = true;
			
			
			// check nearby blocks for same block or same drop
	    	int bx = (event.x >> 2) << 2;
	    	int by = (event.y >> 2) << 2;
	    	int bz = (event.z >> 2) << 2;
	    	
	    	// pre-check for cobble!
			boolean allCobble = event.getState().getBlock().getItemDropped(event.getState(), event.getWorld().rand, 0) == Item.getItemFromBlock(Blocks.COBBLESTONE);
	    	for (int dx = 0; dx < 4; dx++) {
	    		for (int dy = 0; dy < 4; dy++) {
	    			for (int dz = 0; dz < 4; dz++) {
	    				Block blockThere = event.getWorld().getBlock(bx + dx, by + dy, bz + dz);
	    				int metaThere = event.getWorld().getBlockMetadata(bx + dx, by + dy, bz + dz);
						allCobble &= blockThere.getItemDropped(metaThere, event.getWorld().rand, 0) == Item.getItemFromBlock(Blocks.COBBLESTONE);
	    			}
	    		}
	    	}
	    	
	    	if (allCobble && !event.getPlayer().capabilities.isCreativeMode) {
	    		//System.out.println("It's all cobble!");
				this.shouldMakeGiantCobble = true;
				this.amountOfCobbleToReplace = 64;
	    	} else {
				this.shouldMakeGiantCobble = false;
				this.amountOfCobbleToReplace = 0;
	    	}

	    	// break all nearby blocks
	    	for (int dx = 0; dx < 4; dx++) {
	    		for (int dy = 0; dy < 4; dy++) {
	    			for (int dz = 0; dz < 4; dz++) {
	    				Block blockThere = event.getWorld().getBlock(bx + dx, by + dy, bz + dz);
	    				int metaThere = event.getWorld().getBlockMetadata(bx + dx, by + dy, bz + dz);
	    						
	    				if (!(event.x == bx + dx && event.y == by + dy && event.z == bz + dz) && blockThere == event.block && metaThere == event.blockMetadata) {
	    					// try to break that block too!
	    					if (event.getPlayer() instanceof EntityPlayerMP) {
	    						EntityPlayerMP playerMP = (EntityPlayerMP)event.getPlayer();
	    						
	    						playerMP.interactionManager.tryHarvestBlock(bx + dx, by + dy, bz + dz);
	    					}
	    				}
	    			}
	    		}
	    	}
	    	
			this.isBreakingWithGiantPick = false;

		}
	}
	
	/**
	 * Check if the player is trying to right block a block in a structure that's considered protected
	 * Also check for fiery set achievement
	 */
	@SubscribeEvent
	public void rightClickBlock(PlayerInteractEvent event) {
		if (event.action == Action.RIGHT_CLICK_BLOCK && event.getEntityPlayer().worldObj.provider instanceof WorldProviderTwilightForest && !event.getEntityPlayer().capabilities.isCreativeMode) {

			World world = event.getEntityPlayer().worldObj;
			EntityPlayer player = event.getEntityPlayer();
			int x = event.x;
			int y = event.y;
			int z = event.z;

			if (!world.isRemote && isBlockProtectedFromInteraction(world, x, y, z) && isAreaProtected(world, player, x, y, z)) {
				event.useBlock = Result.DENY;
			}
		}
		
		ItemStack currentItem = event.getEntityPlayer().inventory.getCurrentItem();
		if (currentItem != null && (currentItem.getItem() == TFItems.fierySword || currentItem.getItem() == TFItems.fieryPick)) {
			// are they also wearing the armor
			if (checkPlayerForFieryArmor(event.getEntityPlayer())) {
				event.getEntityPlayer().addStat(TFAchievementPage.twilightFierySet);
			}
		}
	}

	/**
	 * Stop the player from interacting with blocks that could produce treasure or open doors in a protected area
	 */
	private boolean isBlockProtectedFromInteraction(World world, int x, int y, int z) {
		Block block = world.getBlock(x, y, z);
		
		if (block == TFBlocks.towerDevice || block == Blocks.CHEST || block == Blocks.TRAPPED_CHEST
				|| block == Blocks.STONE_BUTTON || block == Blocks.WOODEN_BUTTON || block == Blocks.LEVER) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Stop the player from breaking blocks.  We protect all blocks except openblocks graves
	 */
	private boolean isBlockProtectedFromBreaking(World world, int x, int y, int z) {
		Block block = world.getBlock(x, y, z);
		
		// graves are okay!
		if (block.getUnlocalizedName().equals("tile.openblocks.grave")) {
			return false;
		}
		
		return true;
	}

	/**
	 * Return true if the player is wearing at least one piece of fiery armor
	 */
	private boolean checkPlayerForFieryArmor(EntityPlayer entityPlayer) {
		ItemStack[] armor = entityPlayer.inventory.armorInventory;
		if (armor[0] != null && armor[0].getItem() == TFItems.fieryBoots) {
			return true;
		} else if (armor[1] != null && armor[1].getItem() == TFItems.fieryLegs) {
			return true;
		} else if (armor[2] != null && armor[2].getItem() == TFItems.fieryPlate) {
			return true;
		} else if (armor[3] != null && armor[3].getItem() == TFItems.fieryHelm) {
			return true;
		} else  {
			return false;
		}
	}

	/**
	 * Return if the area at the coordinates is considered protected for that player.
	 * Currently, if we return true, we also send the area protection packet here.
	 */
	private boolean isAreaProtected(World world, EntityPlayer player, int x, int y, int z) {
		if (world.getGameRules().getBoolean(TwilightForestMod.ENFORCED_PROGRESSION_RULE) && world.provider instanceof WorldProviderTwilightForest) {

			ChunkProviderTwilightForest chunkProvider = ((WorldProviderTwilightForest)world.provider).getChunkProvider();
			
			if (chunkProvider != null && chunkProvider.isBlockInStructureBB(x, y, z)) {
				// what feature is nearby?  is it one the player has not unlocked?
				TFFeature nearbyFeature = ((TFWorldChunkManager)world.provider.worldChunkMgr).getFeatureAt(x, z, world);

				if (!nearbyFeature.doesPlayerHaveRequiredAchievement(player) && chunkProvider.isBlockProtected(x, y, z)) {
					
					// send protection packet
					StructureBoundingBox sbb = chunkProvider.getSBBAt(x, y, z);
					sendAreaProtectionPacket(world, x, y, z, sbb);
					
					// send a hint monster?
					nearbyFeature.trySpawnHintMonster(world, player, x, y, z);

					return true;
				}
			}
		}
		return false;
	}

	private void sendAreaProtectionPacket(World world, int x, int y, int z, StructureBoundingBox sbb) {
		// send packet
		FMLProxyPacket message = TFGenericPacketHandler.makeAreaProtectionPacket(sbb, x, y, z);

		NetworkRegistry.TargetPoint targetPoint = new NetworkRegistry.TargetPoint(world.provider.dimensionId, x, y, z, 64);
		
		TwilightForestMod.genericChannel.sendToAllAround(message, targetPoint);
	}

	/**
	 * Cancel attacks in protected areas
	 */
	@SubscribeEvent
	public void livingAttack(LivingAttackEvent event) {

		// area protection check
		if (event.getEntityLiving() instanceof IMob && event.getSource().getEntity() instanceof EntityPlayer && !((EntityPlayer)event.getSource().getEntity()).capabilities.isCreativeMode && event.getEntityLiving().worldObj.provider instanceof WorldProviderTwilightForest && event.getEntityLiving().worldObj.getGameRules().getGameRuleBooleanValue(TwilightForestMod.ENFORCED_PROGRESSION_RULE)) {

			ChunkProviderTwilightForest chunkProvider = ((WorldProviderTwilightForest)event.getEntityLiving().worldObj.provider).getChunkProvider();

			int mx = MathHelper.floor_double(event.getEntityLiving().posX);
			int my = MathHelper.floor_double(event.getEntityLiving().posY);
			int mz = MathHelper.floor_double(event.getEntityLiving().posZ);

			if (chunkProvider != null && chunkProvider.isBlockInStructureBB(mx, my, mz) && chunkProvider.isBlockProtected(mx, my, mz)) {
				// what feature is nearby?  is it one the player has not unlocked?
				TFFeature nearbyFeature = ((TFWorldChunkManager)event.getEntityLiving().worldObj.provider.worldChunkMgr).getFeatureAt(mx, mz, event.getEntityLiving().worldObj);

				if (!nearbyFeature.doesPlayerHaveRequiredAchievement((EntityPlayer) event.getSource().getEntity())) {
					event.setResult(Result.DENY);
					event.setCanceled(true);
					
					
					// particle effect
					for (int i = 0; i < 20; i++) {
			            //worldObj.spawnParticle("mobSpell", blockX + 0.5F, blockY + 0.5F, blockZ + 0.5F, red, grn, blu);
						TwilightForestMod.proxy.spawnParticle(event.getEntityLiving().worldObj, "protection", event.getEntityLiving().posX, event.getEntityLiving().posY, event.getEntityLiving().posZ, 0, 0, 0);

					}
				}
			}
		}
	}
	
    /**
     * When player logs in, report conflict status, set enforced_progression rule
	 */
	@SubscribeEvent
	public void playerLogsIn(PlayerLoggedInEvent event) {
		// check enforced progression
		if (!event.player.worldObj.isRemote && event.player instanceof EntityPlayerMP) {
			this.sendEnforcedProgressionStatus((EntityPlayerMP)event.player, event.player.worldObj.getGameRules().getGameRuleBooleanValue(TwilightForestMod.ENFORCED_PROGRESSION_RULE));
		}
	}
	
    /**
     * When player changes dimensions, send the rule status if they're moving to the Twilight Forest
	 */
	@SubscribeEvent
	public void playerPortals(PlayerChangedDimensionEvent event) {
		// check enforced progression
		if (!event.player.worldObj.isRemote && event.player instanceof EntityPlayerMP && event.toDim == TwilightForestMod.dimensionID) {
			this.sendEnforcedProgressionStatus((EntityPlayerMP)event.player, event.player.worldObj.getGameRules().getGameRuleBooleanValue(TwilightForestMod.ENFORCED_PROGRESSION_RULE));
		}
	}
	
    private void sendEnforcedProgressionStatus(EntityPlayerMP player, boolean isEnforced) {
		TwilightForestMod.genericChannel.sendTo(TFGenericPacketHandler.makeEnforcedProgressionStatusPacket(isEnforced), player);
	}

	/**
     * When world is loaded, check if the game rule is defined
     */
	@SubscribeEvent
	public void worldLoaded(WorldEvent.Load event) {
		// check rule
		if (!event.getWorld().isRemote && !event.getWorld().getGameRules().hasRule(TwilightForestMod.ENFORCED_PROGRESSION_RULE)) {
			FMLLog.info("[TwilightForest] Loaded a world with the tfEnforcedProgression game rule not defined.  Defining it.");
			
			event.getWorld().getGameRules().addGameRule(TwilightForestMod.ENFORCED_PROGRESSION_RULE, "true", GameRules.ValueType.BOOLEAN_VALUE);
		}
	}
	
	/**
     * When a command is used, check if someone's changing the progression game rule
     */
	@SubscribeEvent
	public void commandSent(CommandEvent event) {
		if (event.getCommand() instanceof CommandGameRule && event.getParameters().length > 1 && TwilightForestMod.ENFORCED_PROGRESSION_RULE.equals(event.getParameters()[0])) {
			boolean isEnforced = Boolean.valueOf(event.getParameters()[1]);
			TwilightForestMod.genericChannel.sendToAll(TFGenericPacketHandler.makeEnforcedProgressionStatusPacket(isEnforced));
		}
	}

}
