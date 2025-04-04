package com.hcapps.util.model

import androidx.compose.ui.graphics.Color
import com.hcapps.ui.theme.AngryColor
import com.hcapps.ui.theme.AwfulColor
import com.hcapps.ui.theme.BoredColor
import com.hcapps.ui.theme.CalmColor
import com.hcapps.ui.theme.DepressedColor
import com.hcapps.ui.theme.DisappointedColor
import com.hcapps.ui.theme.HappyColor
import com.hcapps.ui.theme.HumorousColor
import com.hcapps.ui.theme.LonelyColor
import com.hcapps.ui.theme.MysteriousColor
import com.hcapps.ui.theme.NeutralColor
import com.hcapps.ui.theme.RomanticColor
import com.hcapps.ui.theme.ShamefulColor
import com.hcapps.ui.theme.SurprisedColor
import com.hcapps.ui.theme.SuspiciousColor
import com.hcapps.ui.theme.TenseColor
import com.hcapps.util.R

enum class Mood(
    val icon: Int,
    val contentColor: Color,
    val containerColor: Color
) {
    Neutral(
        icon = R.drawable.neutral,
        contentColor = Color.Black,
        containerColor = NeutralColor
    ),
    Happy(
        icon = R.drawable.happy,
        contentColor = Color.Black,
        containerColor = HappyColor
    ),
    Angry(
        icon = R.drawable.angry,
        contentColor = Color.White,
        containerColor = AngryColor
    ),
    Bored(
        icon = R.drawable.bored,
        contentColor = Color.White,
        containerColor = BoredColor
    ),
    Calm(
        icon = R.drawable.calm,
        contentColor = Color.Black,
        containerColor = CalmColor
    ),
    Depressed(
        icon = R.drawable.depressed,
        contentColor = Color.White,
        containerColor = DepressedColor
    ),
    Disappointed(
        icon = R.drawable.disappointed,
        contentColor = Color.White,
        containerColor = DisappointedColor
    ),
    Humorous(
        icon = R.drawable.humorous,
        contentColor = Color.White,
        containerColor = HumorousColor
    ),
    Lonely(
        icon = R.drawable.lonely,
        contentColor = Color.White,
        containerColor = LonelyColor
    ),
    Mysterious(
        icon = R.drawable.mysterious,
        contentColor = Color.White,
        containerColor = MysteriousColor
    ),
    Romantic(
        icon = R.drawable.romantic,
        contentColor = Color.White,
        containerColor = RomanticColor
    ),
    Shameful(
        icon = R.drawable.shameful,
        contentColor = Color.White,
        containerColor = ShamefulColor
    ),
    Awful(
        icon = R.drawable.awful,
        contentColor = Color.White,
        containerColor = AwfulColor
    ),
    Surprised(
        icon = R.drawable.surprised,
        contentColor = Color.White,
        containerColor = SurprisedColor
    ),
    Suspicious(
        icon = R.drawable.suspicious,
        contentColor = Color.White,
        containerColor = SuspiciousColor
    ),
    Tense(
        icon = R.drawable.tense,
        contentColor = Color.Black,
        containerColor = TenseColor
    )
}