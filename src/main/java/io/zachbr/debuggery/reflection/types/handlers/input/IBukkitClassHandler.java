/*
 * This file is part of Debuggery.
 *
 * Debuggery is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Debuggery is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Debuggery.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.zachbr.debuggery.reflection.types.handlers.input;

import io.zachbr.debuggery.reflection.types.handlers.base.IHandler;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class IBukkitClassHandler implements IHandler {

    @Nonnull
    static Class getClass(String input) throws ClassNotFoundException {
        // This is only used for entities right now, so we can save some drama and just search those packages
        // todo above assumption no longer true, figure out what to do here
        final String[] searchPackages = {"org.bukkit", "org.bukkit.entity", "org.bukkit.entity.minecart"};

        String normalized = input;
        if (normalized.endsWith(".class")) {
            normalized = normalized.substring(0, normalized.length() - 6);
        }

        Class clazz = null;
        for (String packageName : searchPackages) {
            try {
                clazz = Class.forName(packageName + "." + normalized);
            } catch (ClassNotFoundException ignored) {
            }

            if (clazz != null) {
                return clazz;
            }
        }

        throw new ClassNotFoundException(normalized + " not present in Bukkit entity namespace");
    }

    @Nonnull
    @Override
    public Class instantiateInstance(String input, Class clazz, @Nullable CommandSender sender) throws ClassNotFoundException {
        return getClass(input); // separate method so that IBukkitClassesHandler can get to it
    }

    @Nonnull
    @Override
    public Class<?> getRelevantClass() {
        return Class.class; // This is only used for entities right now, so we can save some drama and just search those packages
    }
}