package com.dertefter.settings_about.presentation

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import com.dertefter.design.components.appbar.AppToolbar
import com.dertefter.design.components.buttons.AppNavigationIcon
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.spacing
import com.dertefter.settings_about.R
import com.google.android.gms.oss.licenses.v2.OssLicensesMenuActivity

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingsAboutScreen(
    onEvent: (Event) -> Unit,
    isPanel: Boolean = false,
) {

    val context = LocalContext.current

    val packageManager = context.packageManager
    val packageName = context.packageName
    val packageInfo = try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
        } else {
            @Suppress("DEPRECATION")
            packageManager.getPackageInfo(packageName, 0)
        }
    } catch (_: Exception) {
        null
    }
    val versionName = packageInfo?.versionName ?: ""


    val ossTitle = stringResource(R.string.settings_about_oss_licenses)

    fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            context.startActivity(intent)
        } catch (_: Exception) {
            // ignore
        }
    }


    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            AppToolbar(
                title = stringResource(R.string.settings_about_title),
                navigationIcon = {
                    if (!isPanel) {
                        AppNavigationIcon(
                            icon = Icons.ArrowBack,
                            onClick = {
                                onEvent(Event.OnNavigateBack)
                            }
                        )
                    }

                }
            )
        },
    )
    { contentPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(horizontal = MaterialTheme.spacing.defaultScreenPadding)
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            item{
                Column(
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(MaterialTheme.spacing.extraLarge)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "NETI Next",
                        style = MaterialTheme.typography.headlineMediumEmphasized,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = versionName,
                        style = MaterialTheme.typography.labelLargeEmphasized
                    )
                }
            }

            item{
                Column(
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.largeIncreased)
                        .fillMaxWidth()
                ) {
                    ListItem(
                        title = stringResource(R.string.settings_about_privacy_policy),
                        icon = Icons.Policy,
                        onClick = { openUrl("https://docs.google.com/document/d/1D0f0Fp51h_Jj6MZro8nLKvSY2plH1CLZVD4js3ZFSYY/edit?usp=sharingy") }
                    )

                    ListItem(
                        title = stringResource(R.string.settings_about_telegram),
                        icon = Icons.TG,
                        onClick = { openUrl("https://t.me/nstumobile_dev") }
                    )

                    ListItem(
                        title = stringResource(R.string.settings_about_github),
                        icon = Icons.GitHub,
                        onClick = { openUrl("https://github.com/dertefter/NETIClient") }
                    )

                    ListItem(
                        title = stringResource(R.string.settings_about_oss_licenses),
                        icon = Icons.Code,
                        onClick = {
                            OssLicensesMenuActivity.setActivityTitle(ossTitle)
                            context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
                        }
                    )

                }
            }


        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsAboutScreenPreview() {
    AppTheme {
        SettingsAboutScreen(onEvent = {},
        )
    }
}
