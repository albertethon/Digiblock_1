/*
 * Copyright (c) 2016 Helmut Neemann
 * Use of this source code is governed by the GPL v3 license
 * that can be found in the LICENSE file.
 */
package de.neemann.digiblock.core.wiring;

import de.neemann.digiblock.TestExecuter;
import de.neemann.digiblock.core.Model;
import de.neemann.digiblock.core.ObservableValue;
import de.neemann.digiblock.core.element.ElementAttributes;
import de.neemann.digiblock.core.element.Keys;
import junit.framework.TestCase;

/**
 */
public class DecoderTest extends TestCase {

    public void testDecoder() throws Exception {
        Model model = new Model();
        ObservableValue sel = new ObservableValue("sel", 2);
        Decoder decoder = model.add(new Decoder(
                new ElementAttributes()
                        .set(Keys.SELECTOR_BITS, 2)));
        decoder.setInputs(sel.asList());


        TestExecuter te = new TestExecuter(model).setInputs(sel).setOutputs(decoder.getOutputs());
        te.check(0, 1, 0, 0, 0);
        te.check(1, 0, 1, 0, 0);
        te.check(2, 0, 0, 1, 0);
        te.check(3, 0, 0, 0, 1);
    }

}
