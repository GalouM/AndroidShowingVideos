package com.example.showingvideos.library.uicommon

import com.example.showingvideos.library.fetchingvideo.local.model.Duration
import com.example.showingvideos.library.fetchingvideo.local.model.Height
import com.example.showingvideos.library.fetchingvideo.local.model.Id
import com.example.showingvideos.library.fetchingvideo.local.model.Width
import com.example.showingvideos.library.uimodels.VideoUi

val sampleVideoZeroHeight = VideoUi(
    id = Id(3),
    width = Width(1280),
    height = Height(0),
    image = "https://images.pexels.com/videos/3209828/3209828-hd_1280_720_25fps.mp4-M.jpg", // Different valid image
    duration = Duration(90),
    videoFiles = emptyMap()
)

val sampleTallVideo = VideoUi(
    id = Id(2),
    width = Width(1080),
    height = Height(1920),
    image = "https://images.pexels.com/videos/854400/pictures/preview-0.jpg",
    duration = Duration(180),
    videoFiles = emptyMap()
)

val fakeVideoUiList: List<VideoUi> = listOf(
    VideoUi(
        id = Id(1),
        width = Width(1920),
        height = Height(1080),
        image = "https://images.pexels.com/videos/853877/pexels-photo-853877.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500", // Landscape
        duration = Duration(125),
        videoFiles = emptyMap()
    ),
    VideoUi(
        id = Id(2),
        width = Width(1080),
        height = Height(1920),
        image = "https://images.pexels.com/videos/7578506/pexels-photo-7578506.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500", // Portrait
        duration = Duration(72),
        videoFiles = emptyMap()
    ),
    VideoUi(
        id = Id(3),
        width = Width(1280),
        height = Height(720),
        image = "https://images.pexels.com/videos/3254002/pexels-photo-3254002.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500", // Landscape, food
        duration = Duration(90),
        videoFiles = emptyMap()
    ),
    VideoUi(
        id = Id(4),
        width = Width(640),
        height = Height(480),
        image = "https://images.pexels.com/videos/3770303/pexels-photo-3770303.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500", // Abstract
        duration = Duration(45),
        videoFiles = emptyMap()
    )
)