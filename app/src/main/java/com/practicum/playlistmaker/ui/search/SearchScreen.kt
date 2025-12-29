package com.practicum.playlistmaker.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.presentation.AppTrack
import com.practicum.playlistmaker.presentation.SearchState
import com.practicum.playlistmaker.ui.materialTheme.YS
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip

private val ScreenBgLight = Color.White
private val ScreenBgDark = Color(0xFF1A1B22)

private val SearchFieldBgLight = Color(0xFFE6E8EB)
private val SearchFieldBgDark = Color.White

private val PlaceholderLight = Color(0xFFAEAFB4)
private val PlaceholderDark = Color(0xFF1A1B22)

private val TitleBlack = Color(0xFF1A1B22)
private val TitleWhite = Color.White

private val CursorColor = Color(0xFF3772E7)

private val SecondaryLight = Color(0xFFAEAFB4)
private val SecondaryDark = Color.White.copy(alpha = 0.84f)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    isDarkTheme: Boolean,
    onBackClick: () -> Unit
) {
    val query by viewModel.query
    val state by viewModel.state

    val screenBackground = if (!isDarkTheme) ScreenBgLight else ScreenBgDark
    val titleColor = if (!isDarkTheme) TitleBlack else TitleWhite

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(screenBackground)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(start = 16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_left),
                contentDescription = stringResource(R.string.back),
                tint = titleColor,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onBackClick() }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(R.string.search),
                fontFamily = YS,
                fontWeight = FontWeight.Medium,
                fontSize = 22.sp,
                lineHeight = 26.sp,
                color = titleColor
            )
        }

        SearchBar(
            query = query,
            onQueryChange = { viewModel.onQueryChange(it) },
            onSearch = { viewModel.search() },
            onClear = { viewModel.clear() },
            isDarkTheme = isDarkTheme
        )

        when (val s = state) {
            is SearchState.Empty -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {}

            is SearchState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = CursorColor)
            }

            is SearchState.Content -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(s.tracks, key = { it.trackName + it.artistName }) { track ->
                    TrackRow(track, isDarkTheme)
                }
            }

            is SearchState.Error -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 210.dp)
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (!isDarkTheme) R.drawable.ic_failed_search_light
                            else R.drawable.ic_failed_search_dark
                        ),
                        contentDescription = stringResource(R.string.search_failed),
                        modifier = Modifier.size(120.dp),
                        tint = Color.Unspecified
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(R.string.search_failed),
                        fontFamily = YS,
                        fontSize = 16.sp,
                        lineHeight = 19.sp,
                        color = titleColor
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClear: () -> Unit,
    isDarkTheme: Boolean
) {
    val focusManager = LocalFocusManager.current

    val backgroundColor = if (!isDarkTheme) SearchFieldBgLight else SearchFieldBgDark
    val placeholderColor = if (!isDarkTheme) PlaceholderLight else PlaceholderDark
    val textColor = TitleBlack
    val cursorColor = CursorColor

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(8.dp),
            textStyle = androidx.compose.ui.text.TextStyle(
                fontFamily = YS,
                fontSize = 16.sp,
                lineHeight = 19.sp,
                color = textColor
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = backgroundColor,
                unfocusedContainerColor = backgroundColor,
                disabledContainerColor = backgroundColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = cursorColor,
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
            ),
            placeholder = {
                Text(
                    text = stringResource(R.string.search),
                    fontFamily = YS,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    lineHeight = 19.sp,
                    color = placeholderColor
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search_16),
                    contentDescription = stringResource(R.string.search),
                    tint = placeholderColor,
                    modifier = Modifier
                        .size(16.dp)
                        .clickable {
                            onSearch()
                            focusManager.clearFocus()
                        }
                )
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_clear_16),
                        contentDescription = stringResource(R.string.clear),
                        tint = placeholderColor,
                        modifier = Modifier
                            .size(16.dp)
                            .clickable {
                                onClear()
                                focusManager.clearFocus()
                            }
                    )
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                onSearch()
                focusManager.clearFocus()
            })
        )
    }
}

@Composable
fun TrackRow(
    track: AppTrack,
    isDarkTheme: Boolean,
    onClick: () -> Unit = {}
) {
    val itemBackground = if (!isDarkTheme) Color.White else Color(0xFF1A1B22)
    val iconTint = if (!isDarkTheme) TitleBlack else Color.Black
    val textPrimary = if (!isDarkTheme) TitleBlack else TitleWhite
    val textSecondary = if (!isDarkTheme) SecondaryLight else SecondaryDark
    val chevronTint = if (!isDarkTheme) PlaceholderLight else TitleWhite

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(61.dp)
            .clickable { onClick() }
            .padding(horizontal = 13.dp, vertical = 8.dp)
            .background(itemBackground),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_music),
            contentDescription = stringResource(R.string.track_item_description, track.trackName),
            colorFilter = ColorFilter.tint(iconTint),
            modifier = Modifier
                .size(45.dp)
                .clip(CircleShape)
                .background(itemBackground, CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = track.trackName,
                fontFamily = YS,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 19.sp,
                color = textPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = track.artistName,
                    fontFamily = YS,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp,
                    lineHeight = 13.sp,
                    color = textSecondary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Box(
                    modifier = Modifier
                        .size(3.dp)
                        .background(textSecondary, shape = CircleShape)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = track.trackTime,
                    fontFamily = YS,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp,
                    lineHeight = 13.sp,
                    color = textSecondary
                )
            }
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_chevron_right),
            contentDescription = null,
            tint = chevronTint,
            modifier = Modifier.size(24.dp)
        )
    }
}
