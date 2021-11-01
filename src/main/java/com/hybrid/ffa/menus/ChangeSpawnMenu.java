package com.hybrid.ffa.menus;

import net.hybrid.core.utility.CC;
import net.hybrid.core.utility.bookgui.BookUtil;
import org.bukkit.inventory.ItemStack;

public class ChangeSpawnMenu {

    public static ItemStack getChangeSpawnBook() {
        return BookUtil.writtenBook()
                .author("Hybrid").title("Change Location").pages(new BookUtil.PageBuilder()
                        .add("Choose a new spawning location:")

                        .newLine().newLine()

                        .add(BookUtil.TextBuilder.of("➟ §6§lDesert")
                                .onHover(BookUtil.HoverAction.showText(CC.translate("&7Click to choose")))
                                .onClick(BookUtil.ClickAction.runCommand("/spawnlocation desert"))
                                .build())

                        .newLine().newLine()

                        .add(BookUtil.TextBuilder.of("➟ §7§lWinter")
                                .onHover(BookUtil.HoverAction.showText(CC.translate("&7Click to choose")))
                                .onClick(BookUtil.ClickAction.runCommand("/spawnlocation winter"))
                                .build())

                        .newLine().newLine()

                        .add(BookUtil.TextBuilder.of("➟ §2§lJungle")
                                .onHover(BookUtil.HoverAction.showText(CC.translate("&7Click to choose")))
                                .onClick(BookUtil.ClickAction.runCommand("/spawnlocation jungle"))
                                .build())

                        .newLine().newLine()

                        .add(BookUtil.TextBuilder.of("➟ §a§lPlains")
                                .onHover(BookUtil.HoverAction.showText(CC.translate("&7Click to choose")))
                                .onClick(BookUtil.ClickAction.runCommand("/spawnlocation plains"))
                                .build())

                        .build()
                )
                .build();
    }

}
