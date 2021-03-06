package com.intellij.flex.completion;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.flex.util.FlexTestUtils;
import com.intellij.javascript.flex.css.FlexStylesIndexableSetContributor;
import com.intellij.javascript.flex.mxml.schema.FlexSchemaHandler;
import com.intellij.lang.javascript.BaseJSCompletionInTextFieldTest;
import com.intellij.lang.javascript.JavaScriptSupportLoader;
import com.intellij.lang.javascript.completion.JSKeywordsCompletionProvider;
import com.intellij.lang.javascript.flex.FlexModuleType;
import com.intellij.lang.javascript.psi.JSExpressionCodeFragment;
import com.intellij.lang.javascript.psi.JSFile;
import com.intellij.lang.javascript.psi.ecmal4.JSClass;
import com.intellij.lang.javascript.psi.impl.JSChangeUtil;
import com.intellij.lang.javascript.psi.impl.JSPsiImplUtils;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.vfs.newvfs.impl.VfsRootAccess;
import com.intellij.util.ArrayUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import static com.intellij.openapi.vfs.VfsUtilCore.convertFromUrl;
import static com.intellij.openapi.vfs.VfsUtilCore.urlToPath;

public abstract class FlexCompletionInTextFieldBase extends BaseJSCompletionInTextFieldTest {

  protected static final String BASE_PATH = "/js2_completion/";

  static final String[] DEFALUT_VALUES =
    ArrayUtil.mergeArrays(JSKeywordsCompletionProvider.TYPE_LITERAL_VALUES, "NaN", "Infinity");

  @Override
  protected ModuleType getModuleType() {
    return FlexModuleType.getInstance();
  }

  protected void setUpJdk() {
    FlexTestUtils.setupFlexSdk(myModule, getTestName(false), getClass());
  }

  protected String getExtension() {
    return "js2";
  }

  protected String getBasePath() {
    return BASE_PATH;
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    VfsRootAccess.allowRootAccess(getTestRootDisposable(),
                                  urlToPath(convertFromUrl(FlexSchemaHandler.class.getResource("z.xsd"))),
                                  urlToPath(convertFromUrl(FlexStylesIndexableSetContributor.class.getResource("FlexStyles.as"))));
  }

  protected void checkTextFieldCompletion(JSExpressionCodeFragment fragment,
                                          String[] included,
                                          String[] excluded,
                                          @Nullable String choose,
                                          String file) throws Exception {
    doTestTextFieldFromFile(fragment, file);
    assertContains(myItems, true, included);
    assertContains(myItems, false, excluded);
    if (choose != null) {
      boolean found = false;
      for (LookupElement item : myItems) {
        if (choose.equals(item.getLookupString())) {
          selectItem(item);
          found = true;
          break;
        }
      }
      assertTrue("Item '" + choose + "' not found in lookup", found);
      checkResultByFile(BASE_PATH + getTestName(false) + "_after.txt");
    }
  }

  private static void assertContains(LookupElement[] items, boolean contains, String... expected) {
    Collection<String> c = new HashSet<>(Arrays.asList(expected));
    for (LookupElement item : items) {
      final String s = item.getLookupString();
      final boolean removed = c.remove(s);
      if (!contains) {
        assertTrue("'" + s + "' is not expected to be part of completion list", !removed);
      }
    }
    if (contains) {
      assertTrue("Items [" + toString(c, ",") + "] are expected to be part of completion list", c.isEmpty());
    }
  }

  protected JSClass createFakeClass() {
    return JSPsiImplUtils.findClass((JSFile)JSChangeUtil
      .createJSTreeFromText(myProject, "package {class Foo { function a() {}} }", JavaScriptSupportLoader.ECMA_SCRIPT_L4)
      .getPsi().getContainingFile());
  }
}
