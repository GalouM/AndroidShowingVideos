package com.example.showingvideos.library.fetchingvideo.local

import com.example.showingvideos.library.fetchingvideo.local.model.Duration
import com.example.showingvideos.library.fetchingvideo.local.model.Height
import com.example.showingvideos.library.fetchingvideo.local.model.Id
import com.example.showingvideos.library.fetchingvideo.local.model.User
import com.example.showingvideos.library.fetchingvideo.local.model.Video
import com.example.showingvideos.library.fetchingvideo.local.model.VideoFile
import com.example.showingvideos.library.fetchingvideo.local.model.VideoPicture
import com.example.showingvideos.library.fetchingvideo.local.model.Width
import com.example.showingvideos.library.fetchingvideo.remote.apimodel.UserApi
import com.example.showingvideos.library.fetchingvideo.remote.apimodel.VideoFileApi
import com.example.showingvideos.library.fetchingvideo.remote.apimodel.VideoListApi
import com.example.showingvideos.library.fetchingvideo.remote.apimodel.VideoPictureApi
import javax.inject.Inject

class VideoMapper @Inject constructor() {

    fun apiModelToLocal(apiModel: VideoListApi): List<Video> =
        apiModel.videos.map { videoApi ->
            Video(
                duration = Duration(videoApi.duration.toLong()),
                height = Height(videoApi.height),
                width = Width(videoApi.width),
                id = Id(videoApi.id),
                image = videoApi.image,
                url = videoApi.url,
                user = apiUserToLocal(videoApi.user),
                videoFiles = apiVideoFilesToLocal(videoApi.videoFiles),
                videoPictures = apiVideoPicturesToLocal(videoApi.videoPictures)
            )
        }

    private fun apiUserToLocal(apiUser: UserApi): User =
        User(
            id = Id(apiUser.id),
            name = apiUser.name,
            url = apiUser.url
        )

    private fun apiVideoFilesToLocal(apiVideoFiles: List<VideoFileApi>): List<VideoFile> =
        apiVideoFiles.map { videoFileApi ->
            VideoFile(
                fileType = videoFileApi.fileType,
                height = Height(videoFileApi.height),
                width = Width(videoFileApi.width),
                id = Id(videoFileApi.id),
                link = videoFileApi.link,
                quality = videoFileApi.quality
            )
        }

    private fun apiVideoPicturesToLocal(apiVideoPictures: List<VideoPictureApi>): List<VideoPicture> =
        apiVideoPictures.map { videoPictureApi ->
            VideoPicture(
                id = Id(videoPictureApi.id),
                nr = videoPictureApi.nr,
                picture = videoPictureApi.picture
            )
        }
}