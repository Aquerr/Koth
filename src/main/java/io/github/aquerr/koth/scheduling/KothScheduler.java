package io.github.aquerr.koth.scheduling;

import io.github.aquerr.koth.Koth;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.scheduler.TaskExecutorService;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * A wrapper around Sponge Scheduler which adds additional functionality used by KOTH.
 */
public class KothScheduler
{
    private static KothScheduler INSTANCE = new KothScheduler();
    private final Scheduler scheduler = Sponge.asyncScheduler();
    private final Koth plugin = Koth.getInstance();

    private final TaskExecutorService syncExecutor = scheduler.createExecutor(plugin.getPluginContainer());
    private final TaskExecutorService asyncExecutor = Sponge.server().scheduler().createExecutor(plugin.getPluginContainer());

    public static KothScheduler getInstance()
    {
        return INSTANCE;
    }

    public void runSyncWithInterval(final long interval, final TimeUnit timeUnit, final Runnable runnable)
    {
        syncExecutor.schedule(runnable, interval, timeUnit);
    }

    public void runAsyncWithInterval(final long interval, final TimeUnit timeUnit, final Runnable runnable)
    {
        asyncExecutor.schedule(runnable, interval, timeUnit);
    }

    public void runAsyncWithInterval(final long interval, final TimeUnit timeUnit, final Callable<Task> consumer)
    {
        asyncExecutor.schedule(consumer, interval, timeUnit);
    }

    public Scheduler getUnderlyingScheduler()
    {
        return this.scheduler;
    }

    public TaskExecutorService getAsyncExecutor()
    {
        return asyncExecutor;
    }

    public TaskExecutorService getSyncExecutor()
    {
        return syncExecutor;
    }
}
