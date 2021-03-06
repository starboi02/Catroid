/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2020 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catrobat.catroid.uiespresso.content.brick.stage;

import android.content.Intent;

import org.catrobat.catroid.R;
import org.catrobat.catroid.common.Constants;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.bricks.OpenUrlBrick;
import org.catrobat.catroid.ui.SpriteActivity;
import org.catrobat.catroid.uiespresso.util.actions.CustomActions;
import org.catrobat.catroid.uiespresso.util.rules.FragmentActivityTestRule;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.espresso.intent.Intents;

import static org.catrobat.catroid.uiespresso.content.brick.utils.BrickDataInteractionWrapper.onBrickAtPosition;
import static org.catrobat.catroid.uiespresso.content.brick.utils.BrickTestUtils.createProjectAndGetStartScript;
import static org.hamcrest.Matchers.allOf;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class OpenUrlBrickTest {

	private int openUrlBrickPosition;
	private final String url = Constants.MAIN_URL_HTTPS;

	@Rule
	public FragmentActivityTestRule<SpriteActivity> baseActivityTestRule = new
			FragmentActivityTestRule<>(SpriteActivity.class, SpriteActivity.EXTRA_FRAGMENT_POSITION, SpriteActivity.FRAGMENT_SCRIPTS);

	@Before
	public void setUp() {
		openUrlBrickPosition = 1;
		Script script = createProjectAndGetStartScript("openUrlBrickTest");
		script.addBrick(new OpenUrlBrick());
		baseActivityTestRule.launchActivity(new Intent());
	}

	@Test
	public void testOpenUrlIntent() {
		onBrickAtPosition(openUrlBrickPosition).onFormulaTextField(R.id.brick_open_url_edit_text).performEnterString(url);
		Intents.init();
		onView(withId(R.id.button_play)).perform(click());
		onView(isRoot()).perform(CustomActions.wait(2000));
		Matcher expectedIntent = allOf(hasAction(Intent.ACTION_VIEW), hasData(url));
		intended(expectedIntent);
		Intents.release();
	}
}
