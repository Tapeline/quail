package me.tapeline.quailj.runtime.std.qml.sound;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QJavaAdapter;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;

public class QMLSoundFuncLoadSound extends QBuiltinFunc {

    public QMLSoundFuncLoadSound(Runtime runtime) {
        super(
                "loadSound",
                Collections.singletonList(
                        new FuncArgument(
                                "path",
                                Collections.singletonList(new TypeModifier(TokenType.TYPE_STRING)),
                                false
                        )
                ),
                runtime,
                runtime.memory,
                false
        );
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args) throws RuntimeStriker {
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(args.get("path").toString()));
            clip.open(inputStream);
            return new QJavaAdapter<>(runtime, clip);
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            Runtime.error("Unexpected exception during clip opening:\n" + e +
                    "\n" + e.getLocalizedMessage());
            return QObject.Val();
        }
    }

}
