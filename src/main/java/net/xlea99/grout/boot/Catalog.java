package net.xlea99.grout.boot;

import java.util.List;
import net.xlea99.grout.entry.cc_attached_rat_ticks.CcAttachedRatTicks;
import net.xlea99.grout.entry.cc_bone_flute_checks.CcBoneFluteChecks;
import net.xlea99.grout.entry.cc_rat_pack_minimum.CcRatPackMinimum;
import net.xlea99.grout.entry.cc_deeper_hats.CcDeeperHats;
import net.xlea99.grout.entry.cc_glare_grumpiness.CcGlareGrumpiness;
import net.xlea99.grout.entry.cc_necromium_armor_slowness.CcNecromiumArmorSlowness;
import net.xlea99.grout.entry.cc_flint_piston_striking.CcFlintPistonStriking;
import net.xlea99.grout.entry.twigs_cc_flowering_azaleas.TwigsCcFloweringAzaleas;
import net.xlea99.grout.entry.cc_storage_duct_item_loss.CcStorageDuctItemLoss;
import net.xlea99.grout.entry.cc_storage_duct_span_limit.CcStorageDuctSpanLimit;
import net.xlea99.grout.entry.cc_weapons_follow_the_blade.CcWeaponsFollowTheBlade;
import net.xlea99.grout.entry.cc_crafted_tether_potions.CcCraftedTetherPotions;
import net.xlea99.grout.entry.cc_hidden_trim_icons.CcHiddenTrimIcons;
import net.xlea99.grout.entry.cc_extended_plated_bricks.CcExtendedPlatedBricks;
import net.xlea99.grout.entry.extended_metal_bars.ExtendedMetalBars;
import net.xlea99.grout.entry.ac_immediately_fast_gingerbread_stripes.AcImmediatelyFastGingerbreadStripes;
import net.xlea99.grout.entry.ac_irradiated_glow_sync.AcIrradiatedGlowSync;
import net.xlea99.grout.entry.ac_iris_biome_light.AcIrisBiomeLight;
import net.xlea99.grout.entry.ac_iris_holograms.AcIrisHolograms;
import net.xlea99.grout.entry.ac_radiation_glow_batching.AcRadiationGlowBatching;
import net.xlea99.grout.entry.ac_thornwood_very_slightly_lighter_planks.AcThornwoodVerySlightlyLighterPlanks;
import net.xlea99.grout.entry.ac_flytrap_eats_flies.AcFlytrapEatsFlies;
import net.xlea99.grout.entry.ac_infinite_soda.AcInfiniteSoda;
import net.xlea99.grout.entry.am_bone_serpent_dead_targets.AmBoneSerpentDeadTargets;
import net.xlea99.grout.entry.am_crocodile_basking_poses.AmCrocodileBaskingPoses;
import net.xlea99.grout.entry.am_crocodile_grab_turn.AmCrocodileGrabTurn;
import net.xlea99.grout.entry.am_crocodile_ambush.AmCrocodileAmbush;
import net.xlea99.grout.entry.am_gazelle_movement.AmGazelleMovement;
import net.xlea99.grout.entry.am_gazelle_step_height.AmGazelleStepHeight;
import net.xlea99.grout.entry.am_grizzly_tame_cubs.AmGrizzlyTameCubs;
import net.xlea99.grout.entry.am_grizzly_tempting.AmGrizzlyTempting;
import net.xlea99.grout.entry.am_hummingbird_fixes.AmHummingbirdFixes;
import net.xlea99.grout.entry.am_crimson_mosquito_fixes.AmCrimsonMosquitoFixes;
import net.xlea99.grout.entry.am_slightly_sexier_crimson_mosquito.AmSlightlySexierCrimsonMosquito;
import net.xlea99.grout.entry.am_slightly_sexier_crimson_mosquito_motion.AmSlightlySexierCrimsonMosquitoMotion;
import net.xlea99.grout.entry.am_rattlesnake_fixes.AmRattlesnakeFixes;
import net.xlea99.grout.entry.am_slightly_sexier_rattlesnake.AmSlightlySexierRattlesnake;
import net.xlea99.grout.entry.am_slightly_sexier_rattlesnake_motion.AmSlightlySexierRattlesnakeMotion;
import net.xlea99.grout.entry.am_hummingbird_zips_away.AmHummingbirdZipsAway;
import net.xlea99.grout.entry.am_gorillas_forage_neapolitan_bananas.AmGorillasForageNeapolitanBananas;
import net.xlea99.grout.entry.am_gorilla_silverbacks.AmGorillaSilverbacks;
import net.xlea99.grout.entry.am_slightly_sexier_gorilla.AmSlightlySexierGorilla;
import net.xlea99.grout.entry.am_slightly_sexier_gorilla_motion.AmSlightlySexierGorillaMotion;
import net.xlea99.grout.entry.am_gorilla_pathing.AmGorillaPathing;
import net.xlea99.grout.entry.quark_hollow_log_paths.QuarkHollowLogPaths;
import net.xlea99.grout.entry.am_lava_bottle_dupe.AmLavaBottleDupe;
import net.xlea99.grout.entry.am_lava_vision_fallback.AmLavaVisionFallback;
import net.xlea99.grout.entry.am_slightly_sexier_bone_serpent.AmSlightlySexierBoneSerpent;
import net.xlea99.grout.entry.am_slightly_sexier_bone_serpent_jaw.AmSlightlySexierBoneSerpentJaw;
import net.xlea99.grout.entry.am_slightly_sexier_crocodile.AmSlightlySexierCrocodile;
import net.xlea99.grout.entry.am_slightly_sexier_gazelle.AmSlightlySexierGazelle;
import net.xlea99.grout.entry.am_slightly_sexier_grizzly_bear.AmSlightlySexierGrizzlyBear;
import net.xlea99.grout.entry.am_slightly_sexier_roadrunner.AmSlightlySexierRoadrunner;
import net.xlea99.grout.entry.amendments_cake_slicing.AmendmentsCakeSlicing;
import net.xlea99.grout.entry.amendments_cauldron_dispensing.AmendmentsCauldronDispensing;
import net.xlea99.grout.entry.amendments_hanging_pot_names.AmendmentsHangingPotNames;
import net.xlea99.grout.entry.amendments_lava_drip_layers.AmendmentsLavaDripLayers;
import net.xlea99.grout.entry.advancement_frames_packet_checks.AdvancementFramesPacketChecks;
import net.xlea99.grout.entry.amendments_lava_layers.AmendmentsLavaLayers;
import net.xlea99.grout.entry.amendments_mixed_potion_brewing.AmendmentsMixedPotionBrewing;
import net.xlea99.grout.entry.amendments_potion_dilution.AmendmentsPotionDilution;
import net.xlea99.grout.entry.bucket_mob_flicker.BucketMobFlicker;
import net.xlea99.grout.entry.cauldron_fourths.CauldronFourths;
import net.xlea99.grout.entry.chest_boat_chests.ChestBoatChests;
import net.xlea99.grout.entry.chest_boats_take_any_chest.ChestBoatsTakeAnyChest;
import net.xlea99.grout.entry.chest_minecart_chests.ChestMinecartChests;
import net.xlea99.grout.entry.pack_animal_chests.PackAnimalChests;
import net.xlea99.grout.entry.cc_head_vision.CcHeadVision;
import net.xlea99.grout.entry.cc_immediately_fast_rat_collars.CcImmediatelyFastRatCollars;
import net.xlea99.grout.entry.cc_tether_potion_compat.CcTetherPotionCompat;
import net.xlea99.grout.entry.citadel_animation_smoothing.CitadelAnimationSmoothing;
import net.xlea99.grout.entry.citadel_iris_post_effects.CitadelIrisPostEffects;
import net.xlea99.grout.entry.cc_cannon_backfire.CcCannonBackfire;
import net.xlea99.grout.entry.cc_golden_bucket_compat.CcGoldenBucketCompat;
import net.xlea99.grout.entry.cc_golden_milk_follows_neapolitan.CcGoldenMilkFollowsNeapolitan;
import net.xlea99.grout.entry.clayworks_emi_recipes.ClayworksEmiRecipes;
import net.xlea99.grout.entry.clayworks_flower_pots.ClayworksFlowerPots;
import net.xlea99.grout.entry.clayworks_iris_pot_trims.ClayworksIrisPotTrims;
import net.xlea99.grout.entry.clayworks_no_pot_washing.ClayworksNoPotWashing;
import net.xlea99.grout.entry.clayworks_pot_trim_saving.ClayworksPotTrimSaving;
import net.xlea99.grout.entry.clayworks_trim_palettes.ClayworksTrimPalettes;
import net.xlea99.grout.entry.dye_depot_runes.DyeDepotRunes;
import net.xlea99.grout.entry.dye_depot_rock_candy.DyeDepotRockCandy;
import net.xlea99.grout.entry.easy_anvils_quark_anvils.EasyAnvilsQuarkAnvils;
import net.xlea99.grout.entry.easy_shulker_boxes_dye_depot.EasyShulkerBoxesDyeDepot;
import net.xlea99.grout.entry.easy_shulker_boxes_output_slots.EasyShulkerBoxesOutputSlots;
import net.xlea99.grout.entry.dye_depot_sparklers.DyeDepotSparklers;
import net.xlea99.grout.entry.dye_the_world_glow_lights.DyeTheWorldGlowLights;
import net.xlea99.grout.entry.dye_the_world_open_sacks.DyeTheWorldOpenSacks;
import net.xlea99.grout.entry.emf_haunted_harvest_masks.EmfHauntedHarvestMasks;
import net.xlea99.grout.entry.better_tridents_loyal_drops_last.BetterTridentsLoyalDropsLast;
import net.xlea99.grout.entry.big_polar_bears.BigPolarBears;
import net.xlea99.grout.entry.boidsify.Boidsify;
import net.xlea99.grout.entry.no_deadly_memes.NoDeadlyMemes;
import net.xlea99.grout.entry.haunted_harvest_mask_limit.HauntedHarvestMaskLimit;
import net.xlea99.grout.entry.horseman_quark_horses.HorsemanQuarkHorses;
import net.xlea99.grout.entry.keyframe_part_lookup.KeyframePartLookup;
import net.xlea99.grout.entry.model_part_walk.ModelPartWalk;
import net.xlea99.grout.entry.haunted_harvest_pumpkin_compat.HauntedHarvestPumpkinCompat;
import net.xlea99.grout.entry.haunted_harvest_torch_lanterns.HauntedHarvestTorchLanterns;
import net.xlea99.grout.entry.haunted_harvest_unified_carved_pumpkin.HauntedHarvestUnifiedCarvedPumpkin;
import net.xlea99.grout.entry.enderman_head_reach.EndermanHeadReach;
import net.xlea99.grout.entry.iris_firework_fade.IrisFireworkFade;
import net.xlea99.grout.entry.betterf3_lost_f3_info.BetterF3LostF3Info;
import net.xlea99.grout.entry.connectiblechains_collisions.ConnectibleChainsCollisions;
import net.xlea99.grout.entry.connectiblechains_knots_always_tick.ConnectibleChainsKnotsAlwaysTick;
import net.xlea99.grout.entry.zipline_chain_pass_through.ZiplineChainPassThrough;
import net.xlea99.grout.entry.zipline_server_checks.ZiplineServerChecks;
import net.xlea99.grout.entry.etcetera_easy_anvils_label_costs.EtceteraEasyAnvilsLabelCosts;
import net.xlea99.grout.entry.etcetera_label_wash_stacking.EtceteraLabelWashStacking;
import net.xlea99.grout.entry.etcetera_shimmering_bismuth_trims.EtceteraShimmeringBismuthTrims;
import net.xlea99.grout.entry.faucet_pouring_recipes.FaucetPouringRecipes;
import net.xlea99.grout.entry.lithium_navigation_swaps.LithiumNavigationSwaps;
import net.xlea99.grout.entry.map_atlases_fixes.MapAtlasesFixes;
import net.xlea99.grout.entry.moonlight_jade_contents.MoonlightJadeContents;
import net.xlea99.grout.entry.moonlight_registration_races.MoonlightRegistrationRaces;
import net.xlea99.grout.entry.neapolitan_amendments_milkshakes.NeapolitanAmendmentsMilkshakes;
import net.xlea99.grout.entry.no_supps_jar_fish.NoSuppsJarFish;
import net.xlea99.grout.entry.quark_amethyst_beacon_tint.QuarkAmethystBeaconTint;
import net.xlea99.grout.entry.quark_donkey_chests.QuarkDonkeyChests;
import net.xlea99.grout.entry.quark_armor_swap_respects_locks.QuarkArmorSwapRespectsLocks;
import net.xlea99.grout.entry.quark_chest_minecart_recipe.QuarkChestMinecartRecipe;
import net.xlea99.grout.entry.quark_elytra_duplication_crash.QuarkElytraDuplicationCrash;
import net.xlea99.grout.entry.quark_parrot_egg_dispensing.QuarkParrotEggDispensing;
import net.xlea99.grout.entry.no_end_cave_sounds.NoEndCaveSounds;
import net.xlea99.grout.entry.paper_labels.PaperLabels;
import net.xlea99.grout.entry.quark_tool_restock_enchantments.QuarkToolRestockEnchantments;
import net.xlea99.grout.entry.scholar_amendments_lectern_sync.ScholarAmendmentsLecternSync;
import net.xlea99.grout.entry.scholar_lectern_atlases.ScholarLecternAtlases;
import net.xlea99.grout.entry.scholar_default_packs.ScholarDefaultPacks;
import net.xlea99.grout.entry.scholar_supps_book_piles.ScholarSuppsBookPiles;
import net.xlea99.grout.entry.sexy_runic_glints.SexyRunicGlints;
import net.xlea99.grout.entry.sleep_tight_kill_stats.SleepTightKillStats;
import net.xlea99.grout.entry.ru_broken_sounds.RuBrokenSounds;
import net.xlea99.grout.entry.snowy_spirit_brighter_ginger.SnowySpiritBrighterGinger;
import net.xlea99.grout.entry.snowy_spirit_edible_treats.SnowySpiritEdibleTreats;
import net.xlea99.grout.entry.snowy_spirit_eggnog_nerf.SnowySpiritEggnogNerf;
import net.xlea99.grout.entry.species_bloodlust_all_food.SpeciesBloodlustAllFood;
import net.xlea99.grout.entry.species_coil_culling.SpeciesCoilCulling;
import net.xlea99.grout.entry.species_dyeable_chaindeliers.SpeciesDyeableChaindeliers;
import net.xlea99.grout.entry.species_ghoul_forgets_prey.SpeciesGhoulForgetsPrey;
import net.xlea99.grout.entry.species_kinetic_blasts.SpeciesKineticBlasts;
import net.xlea99.grout.entry.species_stackatick_breeding.SpeciesStackatickBreeding;
import net.xlea99.grout.entry.species_swapper_resets_fall.SpeciesSwapperResetsFall;
import net.xlea99.grout.entry.species_soap_washes_stackaticks.SpeciesSoapWashesStackaticks;
import net.xlea99.grout.entry.species_wicked_spares_pets.SpeciesWickedSparesPets;
import net.xlea99.grout.entry.maggoty_spawn_flies.MaggotySpawnFlies;
import net.xlea99.grout.entry.sounds_maggot_plops.SoundsMaggotPlops;
import net.xlea99.grout.entry.spawn_flies_roam_screen.SpawnFliesRoamScreen;
import net.xlea99.grout.entry.spawn_flies_shovel_swatter.SpawnFliesShovelSwatter;
import net.xlea99.grout.entry.spawn_frogs_eat_flies.SpawnFrogsEatFlies;
import net.xlea99.grout.entry.spawn_stew_repels_flies.SpawnStewRepelsFlies;
import net.xlea99.grout.entry.spawn_ru_pineify_rotten_wood.SpawnRuPineifyRottenWood;
import net.xlea99.grout.entry.srm_personality_snow.SrmPersonalitySnow;
import net.xlea99.grout.entry.sleep_tight_spawn_events.SleepTightSpawnEvents;
import net.xlea99.grout.entry.supplementaries_grouper_open_sides.SupplementariesGrouperOpenSides;
import net.xlea99.grout.entry.supps_blue_bomb_dispensing.SuppsBlueBombDispensing;
import net.xlea99.grout.entry.supps_soap_keeps_trims.SuppsSoapKeepsTrims;
import net.xlea99.grout.entry.supps_soap_lore_first.SuppsSoapLoreFirst;
import net.xlea99.grout.entry.supps_soap_washes_etcetera_labels.SuppsSoapWashesEtceteraLabels;
import net.xlea99.grout.entry.supps_soap_washes_labels.SuppsSoapWashesLabels;
import net.xlea99.grout.entry.twigs_bloodstone_obelisks.TwigsBloodstoneObelisks;
import net.xlea99.grout.entry.twigs_gravel_pebbles.TwigsGravelPebbles;
import net.xlea99.grout.entry.twigs_leaf_twigs.TwigsLeafTwigs;
import net.xlea99.grout.entry.twigs_pebble_dispensing.TwigsPebbleDispensing;
import net.xlea99.grout.entry.vanilla_backport_cc_resin_trims.VanillaBackportCcResinTrims;
import net.xlea99.grout.entry.vanilla_backport_fa_cold_pig.VanillaBackportFaColdPig;
import net.xlea99.grout.entry.vanilla_backport_lightning_rods.VanillaBackportLightningRods;
import net.xlea99.grout.entry.vanilla_backport_pause_pauses_music.VanillaBackportPausePausesMusic;
import net.xlea99.grout.entry.vanilla_backport_resin_trim_icons.VanillaBackportResinTrimIcons;
import net.xlea99.grout.entry.warden_roar_loop.WardenRoarLoop;
import net.xlea99.grout.entry.whiter_labels.WhiterLabels;
import net.xlea99.grout.entry.supps_cannon_comparator.SuppsCannonComparator;
import net.xlea99.grout.entry.supps_continuous_hand_ropes.SuppsContinuousHandRopes;
import net.xlea99.grout.entry.supps_drinkables_sip_pacing.SuppsDrinkablesSipPacing;
import net.xlea99.grout.entry.supps_captured_mob_jitter.SuppsCapturedMobJitter;
import net.xlea99.grout.entry.supps_faucet_cauldron_levels.SuppsFaucetCauldronLevels;
import net.xlea99.grout.entry.supps_faucet_honey.SuppsFaucetHoney;
import net.xlea99.grout.entry.supps_faucet_toggle.SuppsFaucetToggle;
import net.xlea99.grout.entry.supps_gunpowder_lights_nuke.SuppsGunpowderLightsNuke;
import net.xlea99.grout.entry.supps_lumisene_burn_time.SuppsLumiseneBurnTime;
import net.xlea99.grout.entry.supps_moved_block_entities.SuppsMovedBlockEntities;
import net.xlea99.grout.entry.supps_moving_block_contents.SuppsMovingBlockContents;
import net.xlea99.grout.entry.supps_notice_board_comparator.SuppsNoticeBoardComparator;
import net.xlea99.grout.entry.supps_pedestal_compat.SuppsPedestalCompat;
import net.xlea99.grout.entry.supps_slice_maps_keep_the_altimeter.SuppsSliceMapsKeepTheAltimeter;
import net.xlea99.grout.entry.themed_candle_holder_flames.ThemedCandleHolderFlames;
import net.xlea99.grout.entry.zeta_instance_race.ZetaInstanceRace;
import net.xlea99.grout.entry.zetter_canvas_texture_cleanup.ZetterCanvasTextureCleanup;
import net.xlea99.grout.entry.zetter_canvas_timers.ZetterCanvasTimers;
import net.xlea99.grout.entry.zetter_copies_like_books.ZetterCopiesLikeBooks;
import net.xlea99.grout.entry.zetter_easel_sync.ZetterEaselSync;
import net.xlea99.grout.entry.woodworks_emi_recipes.WoodworksEmiRecipes;
import net.xlea99.grout.entry.neapolitan_plantain_spider_bundles.NeapolitanPlantainSpiderBundles;
import net.xlea99.grout.entry.zetter_emi_recipes.ZetterEmiRecipes;
import net.xlea99.grout.entry.zetter_frame_default_size.ZetterFrameDefaultSize;
import net.xlea99.grout.entry.zetter_hovered_paintings_stack.ZetterHoveredPaintingsStack;
import net.xlea99.grout.entry.zetter_missing_painting_drops.ZetterMissingPaintingDrops;
import net.xlea99.grout.entry.zetter_painting_lighting.ZetterPaintingLighting;
import net.xlea99.grout.entry.zetter_painting_view_distance.ZetterPaintingViewDistance;
import net.xlea99.grout.entry.zetter_paintings_stay_put.ZetterPaintingsStayPut;
import net.xlea99.grout.entry.zetter_tooltips.ZetterTooltips;
import net.xlea99.grout.entry.zettify_all_paintings.ZettifyAllPaintings;

/**
 * Every class that declares Grout's entries, handed to the machinery in {@link GroutSpec#CATALOG}. Startup loads
 * each one so its constants register before the freeze.
 *
 * <p>These classes are read before Minecraft's classes are safe to load, so a declaration class holds only its entry
 * and sub-value constants: no Minecraft, NeoForge or other mods' imports. Its gameplay code lives beside it in other
 * classes. The workspace tests check every class listed here.
 */
public final class Catalog {
    private static final List<Class<?>> DECLARATIONS = List.of(
        AcImmediatelyFastGingerbreadStripes.class,
        AcIrradiatedGlowSync.class,
        AcRadiationGlowBatching.class,
        AcIrisHolograms.class,
        AcIrisBiomeLight.class,
        AcThornwoodVerySlightlyLighterPlanks.class,
        AcInfiniteSoda.class,
        AcFlytrapEatsFlies.class,
        AmLavaBottleDupe.class,
        AmSlightlySexierGrizzlyBear.class,
        AmSlightlySexierRoadrunner.class,
        AmSlightlySexierGazelle.class,
        AmGazelleStepHeight.class,
        AmGazelleMovement.class,
        AmSlightlySexierCrocodile.class,
        AmCrocodileAmbush.class,
        AmCrocodileBaskingPoses.class,
        AmCrocodileGrabTurn.class,
        AmSlightlySexierBoneSerpent.class,
        AmSlightlySexierBoneSerpentJaw.class,
        AmBoneSerpentDeadTargets.class,
        AmGrizzlyTempting.class,
        AmGrizzlyTameCubs.class,
        AmHummingbirdFixes.class,
        AmHummingbirdZipsAway.class,
        AmGorillasForageNeapolitanBananas.class,
        AmGorillaSilverbacks.class,
        AmSlightlySexierGorilla.class,
        AmSlightlySexierGorillaMotion.class,
        AmGorillaPathing.class,
        AmCrimsonMosquitoFixes.class,
        AmSlightlySexierCrimsonMosquito.class,
        AmSlightlySexierCrimsonMosquitoMotion.class,
        AmRattlesnakeFixes.class,
        AmSlightlySexierRattlesnake.class,
        AmSlightlySexierRattlesnakeMotion.class,
        QuarkHollowLogPaths.class,
        NoDeadlyMemes.class,
        AmLavaVisionFallback.class,
        AmendmentsCakeSlicing.class,
        AmendmentsCauldronDispensing.class,
        AmendmentsHangingPotNames.class,
        AmendmentsLavaDripLayers.class,
        AmendmentsLavaLayers.class,
        AdvancementFramesPacketChecks.class,
        AmendmentsMixedPotionBrewing.class,
        AmendmentsPotionDilution.class,
        BucketMobFlicker.class,
        BetterTridentsLoyalDropsLast.class,
        CauldronFourths.class,
        ChestBoatChests.class,
        ChestBoatsTakeAnyChest.class,
        ChestMinecartChests.class,
        PackAnimalChests.class,
        CcHeadVision.class,
        CcCraftedTetherPotions.class,
        CcHiddenTrimIcons.class,
        CcImmediatelyFastRatCollars.class,
        CcExtendedPlatedBricks.class,
        CcFlintPistonStriking.class,
        CcDeeperHats.class,
        CcGlareGrumpiness.class,
        CcNecromiumArmorSlowness.class,
        CcAttachedRatTicks.class,
        CcBoneFluteChecks.class,
        CcRatPackMinimum.class,
        CcStorageDuctItemLoss.class,
        CcStorageDuctSpanLimit.class,
        CcTetherPotionCompat.class,
        CcWeaponsFollowTheBlade.class,
        CitadelAnimationSmoothing.class,
        CitadelIrisPostEffects.class,
        CcCannonBackfire.class,
        CcGoldenBucketCompat.class,
        CcGoldenMilkFollowsNeapolitan.class,
        ClayworksFlowerPots.class,
        ClayworksEmiRecipes.class,
        ClayworksNoPotWashing.class,
        ClayworksPotTrimSaving.class,
        ClayworksTrimPalettes.class,
        ClayworksIrisPotTrims.class,
        DyeDepotRunes.class,
        DyeDepotRockCandy.class,
        EasyAnvilsQuarkAnvils.class,
        EasyShulkerBoxesDyeDepot.class,
        EasyShulkerBoxesOutputSlots.class,
        DyeDepotSparklers.class,
        DyeTheWorldGlowLights.class,
        DyeTheWorldOpenSacks.class,
        EmfHauntedHarvestMasks.class,
        HauntedHarvestMaskLimit.class,
        HorsemanQuarkHorses.class,
        Boidsify.class,
        KeyframePartLookup.class,
        ModelPartWalk.class,
        HauntedHarvestPumpkinCompat.class,
        HauntedHarvestTorchLanterns.class,
        HauntedHarvestUnifiedCarvedPumpkin.class,
        BigPolarBears.class,
        EndermanHeadReach.class,
        IrisFireworkFade.class,
        BetterF3LostF3Info.class,
        ZiplineServerChecks.class,
        ZiplineChainPassThrough.class,
        ConnectibleChainsCollisions.class,
        ConnectibleChainsKnotsAlwaysTick.class,
        EtceteraEasyAnvilsLabelCosts.class,
        EtceteraLabelWashStacking.class,
        EtceteraShimmeringBismuthTrims.class,
        FaucetPouringRecipes.class,
        LithiumNavigationSwaps.class,
        MapAtlasesFixes.class,
        ExtendedMetalBars.class,
        MoonlightJadeContents.class,
        MoonlightRegistrationRaces.class,
        NeapolitanAmendmentsMilkshakes.class,
        NeapolitanPlantainSpiderBundles.class,
        NoSuppsJarFish.class,
        QuarkAmethystBeaconTint.class,
        QuarkDonkeyChests.class,
        QuarkChestMinecartRecipe.class,
        QuarkArmorSwapRespectsLocks.class,
        QuarkElytraDuplicationCrash.class,
        QuarkParrotEggDispensing.class,
        NoEndCaveSounds.class,
        PaperLabels.class,
        QuarkToolRestockEnchantments.class,
        RuBrokenSounds.class,
        ScholarAmendmentsLecternSync.class,
        ScholarLecternAtlases.class,
        ScholarDefaultPacks.class,
        ScholarSuppsBookPiles.class,
        SexyRunicGlints.class,
        SleepTightKillStats.class,
        SleepTightSpawnEvents.class,
        SnowySpiritBrighterGinger.class,
        SnowySpiritEdibleTreats.class,
        SnowySpiritEggnogNerf.class,
        SpeciesBloodlustAllFood.class,
        SpeciesCoilCulling.class,
        SpeciesDyeableChaindeliers.class,
        SpeciesGhoulForgetsPrey.class,
        SpeciesKineticBlasts.class,
        SpeciesStackatickBreeding.class,
        SpeciesSwapperResetsFall.class,
        SpeciesSoapWashesStackaticks.class,
        SpeciesWickedSparesPets.class,
        MaggotySpawnFlies.class,
        SoundsMaggotPlops.class,
        SpawnFliesRoamScreen.class,
        SpawnFrogsEatFlies.class,
        SpawnFliesShovelSwatter.class,
        SpawnStewRepelsFlies.class,
        SpawnRuPineifyRottenWood.class,
        SrmPersonalitySnow.class,
        SupplementariesGrouperOpenSides.class,
        SuppsBlueBombDispensing.class,
        SuppsSoapKeepsTrims.class,
        SuppsSoapLoreFirst.class,
        SuppsSoapWashesEtceteraLabels.class,
        SuppsSoapWashesLabels.class,
        WardenRoarLoop.class,
        WhiterLabels.class,
        SuppsCannonComparator.class,
        SuppsCapturedMobJitter.class,
        SuppsContinuousHandRopes.class,
        SuppsDrinkablesSipPacing.class,
        SuppsFaucetCauldronLevels.class,
        SuppsFaucetHoney.class,
        SuppsFaucetToggle.class,
        SuppsGunpowderLightsNuke.class,
        SuppsLumiseneBurnTime.class,
        SuppsMovedBlockEntities.class,
        SuppsMovingBlockContents.class,
        SuppsNoticeBoardComparator.class,
        SuppsPedestalCompat.class,
        SuppsSliceMapsKeepTheAltimeter.class,
        ThemedCandleHolderFlames.class,
        TwigsBloodstoneObelisks.class,
        TwigsCcFloweringAzaleas.class,
        TwigsGravelPebbles.class,
        TwigsLeafTwigs.class,
        TwigsPebbleDispensing.class,
        VanillaBackportCcResinTrims.class,
        VanillaBackportFaColdPig.class,
        VanillaBackportLightningRods.class,
        VanillaBackportPausePausesMusic.class,
        VanillaBackportResinTrimIcons.class,
        WoodworksEmiRecipes.class,
        ZetaInstanceRace.class,
        ZetterCanvasTextureCleanup.class,
        ZetterCanvasTimers.class,
        ZetterCopiesLikeBooks.class,
        ZetterEaselSync.class,
        ZetterEmiRecipes.class,
        ZetterFrameDefaultSize.class,
        ZetterHoveredPaintingsStack.class,
        ZetterMissingPaintingDrops.class,
        ZetterPaintingLighting.class,
        ZetterPaintingViewDistance.class,
        ZetterPaintingsStayPut.class,
        ZetterTooltips.class,
        ZettifyAllPaintings.class
    );

    private Catalog() {
    }

    public static List<Class<?>> declarations() {
        return DECLARATIONS;
    }
}
