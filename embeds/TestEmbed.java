package embeds;

import java.util.*;
import me.tapeline.quailj.libmanagement.Embed;
import me.tapeline.quailj.libmanagement.EmbedLoader;
import me.tapeline.quailj.platformspecific.IOManager;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;
import me.tapeline.quailj.libmanagement.*;


public class TestEmbed implements me.tapeline.quailj.libmanagement.Embed {

    static class TestEmbedFuncNanos extends FuncType {
        public TestEmbedFuncNanos() {
            super("nanos", Collections.singletonList(""), null);
        }

        @Override
        public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
            return new NumType(System.nanoTime());
        }

        @Override
        public QType copy() {
            return new TestEmbedFuncNanos();
        }
    }

    public String getName() {
        return "TestEmbed";
    }

    public String getProvider() {
        return "me.tapeline.test";
    }

    public void integrate(me.tapeline.quailj.libmanagement.EmbedIntegrator integrator) {
        integrator.runtime.scope.set("nanos", new TestEmbedFuncNanos());
    }

}