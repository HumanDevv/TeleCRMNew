package com.tele.crm.domain.di

import com.tele.crm.data.datastore.AppDataStore
import com.tele.crm.data.network.model.campaign.CampaignRequest
import com.tele.crm.domain.appevents.AppEventsHandler
import com.tele.crm.domain.appevents.AppEventsHandlerImpl
import com.tele.crm.data.network.repository.CRMRepository
import com.tele.crm.domain.usecases.addLead.AddLeadUseCase
import com.tele.crm.domain.usecases.addLead.AddLeadUseCaseImpl
import com.tele.crm.domain.usecases.addLeadToCampaign.AddLeadToCampaignUseCase
import com.tele.crm.domain.usecases.addLeadToCampaign.AddLeadToCampaignUseCaseImpl
import com.tele.crm.domain.usecases.callLogs.GetCallLogUseCaseImpl
import com.tele.crm.domain.usecases.callLogs.GetCallLogUsecase
import com.tele.crm.domain.usecases.createCampaign.CreateCampaignUseCase
import com.tele.crm.domain.usecases.createCampaign.CreateCampaignUseCaseImpl
import com.tele.crm.domain.usecases.getCampaign.GetCampaignUseCase
import com.tele.crm.domain.usecases.getCampaign.GetCampaignUseCaseImpl
import com.tele.crm.domain.usecases.getCampaignDetails.GetCampaignDetailsUseCase
import com.tele.crm.domain.usecases.getCampaignDetails.GetCampaignDetailsUseCaseImpl
import com.tele.crm.domain.usecases.getInterest.GetInterestUseCase
import com.tele.crm.domain.usecases.getInterest.GetInterestUseCaseImpl
import com.tele.crm.domain.usecases.getLeads.GetLeadUseCase
import com.tele.crm.domain.usecases.getLeads.GetLeadUseCaseImpl
import com.tele.crm.domain.usecases.getProfile.GetProfileUseCase
import com.tele.crm.domain.usecases.getProfile.GetProfileUseCaseImpl

import com.tele.crm.domain.usecases.getStream.GetStreamUseCase
import com.tele.crm.domain.usecases.getStream.GetStreamUseCaseImpl
import com.tele.crm.domain.usecases.getYears.GetYearUseCase
import com.tele.crm.domain.usecases.getYears.GetYearUseCaseImpl
import com.tele.crm.domain.usecases.login.LoginUseCase
import com.tele.crm.domain.usecases.login.LoginUseCaseImpl
import com.tele.crm.domain.usecases.logout.LogOutUseCase
import com.tele.crm.domain.usecases.logout.LogOutUseCaseImpl
import com.tele.crm.domain.usecases.recentCalls.RecentCallUseCase
import com.tele.crm.domain.usecases.recentCalls.RecentCallUseCaseImpl
import com.tele.crm.domain.usecases.updateLeadStatus.UpdateStatusUseCase
import com.tele.crm.domain.usecases.updateLeadStatus.UpdateStatusUseCaseImpl

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    fun provideLoginUserCase(webRepository: CRMRepository,appDataStore: AppDataStore): LoginUseCase =
        LoginUseCaseImpl(webRepository,appDataStore)

  @Provides
    fun provideAddLeadUseCase(webRepository: CRMRepository): AddLeadUseCase =
        AddLeadUseCaseImpl(webRepository)

  @Provides
    fun provideAddLeadToCampaignUseCase(webRepository: CRMRepository): AddLeadToCampaignUseCase =
      AddLeadToCampaignUseCaseImpl(webRepository)

  @Provides
    fun provideUpdateLeadStatusUseCase(webRepository: CRMRepository): UpdateStatusUseCase =
      UpdateStatusUseCaseImpl(webRepository)

  @Provides
    fun provideCampaignUseCase(webRepository: CRMRepository): CreateCampaignUseCase =
      CreateCampaignUseCaseImpl(webRepository)

  @Provides
    fun provideInterestUseCase(webRepository: CRMRepository): GetInterestUseCase =
        GetInterestUseCaseImpl(webRepository)

  @Provides
    fun provideStreamUseCase(webRepository: CRMRepository): GetStreamUseCase =
        GetStreamUseCaseImpl(webRepository)


  @Provides
    fun provideYearsUseCase(webRepository: CRMRepository): GetYearUseCase =
        GetYearUseCaseImpl(webRepository)

  @Provides
    fun provideGetCampaignUseCase(webRepository: CRMRepository): GetCampaignUseCase =
      GetCampaignUseCaseImpl(webRepository)

  @Provides
    fun provideGetLeadUseCase(webRepository: CRMRepository): GetLeadUseCase =
        GetLeadUseCaseImpl(webRepository)
  @Provides
    fun provideLogOutUseCase(webRepository: CRMRepository): LogOutUseCase =
        LogOutUseCaseImpl(webRepository)

    @Provides
    fun provideRecentCallsUseCase(webRepository: CRMRepository): RecentCallUseCase =
        RecentCallUseCaseImpl(webRepository)

    @Provides
    fun provideCallLogssUseCase(webRepository: CRMRepository): GetCallLogUsecase =
        GetCallLogUseCaseImpl(webRepository)

  @Provides
    fun provideProfileUseCase(webRepository: CRMRepository,appDataStore: AppDataStore): GetProfileUseCase =
        GetProfileUseCaseImpl(webRepository,appDataStore)


  @Provides
    fun provideCampaignDetailsUseCase(webRepository: CRMRepository): GetCampaignDetailsUseCase =
        GetCampaignDetailsUseCaseImpl(webRepository)


}
