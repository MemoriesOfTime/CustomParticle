package cn.cookiestudio.customparticle.command;

import cn.cookiestudio.customparticle.CustomParticlePlugin;
import cn.cookiestudio.customparticle.util.Identifier;
import cn.lanink.gamecore.form.windows.AdvancedFormWindowCustom;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.form.element.ElementStepSlider;
import cn.nukkit.level.Position;

import java.util.ArrayList;
import java.util.List;

public class CustomParticleCommand extends Command {
    public CustomParticleCommand(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] str) {
        if (commandSender instanceof ConsoleCommandSender)
            return true;
        if ("play".equals(str[0])) {
            if (!commandSender.isOp())
                return true;
            Identifier identifier = Identifier.fromString(str[1]);
            if (str.length <= 2){
                CustomParticlePlugin.getInstance().getCustomParticlePool().get(identifier).play((Position)commandSender, true);
            }else{
                if ("false".equals(str[2])){
                    CustomParticlePlugin.getInstance().getCustomParticlePool().get(identifier).play((Position)commandSender, false);
                }else if ("true".equals(str[2])){
                    CustomParticlePlugin.getInstance().getCustomParticlePool().get(identifier).play((Position)commandSender, true);
                }
            }
            return true;
        }else if ("reload".equals(str[0])) {
            if (!commandSender.isOp())
                return true;
            CustomParticlePlugin.getInstance().reloadParticle();
        }else if ("clear".equals(str[0])) {
            if (!commandSender.isOp())
                return true;
            Server.getInstance().getScheduler().cancelTask(CustomParticlePlugin.getInstance());
        }else if ("list".equals(str[0])) {
            if (!commandSender.isOp())
                return true;
            commandSender.sendMessage("§eParticle List:");
            CustomParticlePlugin.getInstance().getCustomParticlePool().getCustomParticlePool().forEach((id, p) -> {
                commandSender.sendMessage("ID: §a" + id);
            });
        }else if ("setting".equals(str[0])) {
            Player player = (Player)commandSender;
            List<String> steps = new ArrayList<>();
            java.text.DecimalFormat df = new java.text.DecimalFormat("#.#");
            for (double i = 1.0;i <= 10.0;i += 0.1) {
                steps.add(String.valueOf(df.format(i)));
            }
            AdvancedFormWindowCustom custom = new AdvancedFormWindowCustom("粒子播放设置");
            custom.addElement(new ElementStepSlider(
                    "设置粒子播放密度(数值越大密度越小)",
                    steps,
                    (int)(CustomParticlePlugin.getInstance().getParticleSender().getSetting().getDouble(player.getName()) / 0.1 - 10))
            );
            custom.onResponded((formResponseCustom, cp) -> {
                CustomParticlePlugin.getInstance().getParticleSender().getSetting().set(player.getName(),Integer.valueOf(formResponseCustom.getStepSliderResponse(0).getElementContent()));
                CustomParticlePlugin.getInstance().getParticleSender().getSetting().save();
                CustomParticlePlugin.getInstance().getParticleSender().reloadSettingFile();
                cp.sendMessage("Successfully change setting");
            });
            player.showFormWindow(custom);
        }
        return true;
    }
}
