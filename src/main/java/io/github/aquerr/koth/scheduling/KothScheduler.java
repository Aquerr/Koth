package io.github.aquerr.koth.scheduling;

import io.github.aquerr.koth.Koth;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.scheduler.SpongeExecutorService;
import org.spongepowered.api.scheduler.Task;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * A wrapper around Sponge Scheduler which adds additional functionality used by KOTH.
 */
public class KothScheduler
{
    private static KothScheduler INSTANCE = new KothScheduler();
    private final Scheduler scheduler = Sponge.getScheduler();
    private final Koth plugin = Koth.getInstance();

    private final SpongeExecutorService syncExecutor = scheduler.createSyncExecutor(plugin);
    private final SpongeExecutorService asyncExecutor = scheduler.createAsyncExecutor(plugin);

    public static KothScheduler getInstance()
    {
        return INSTANCE;
    }

    public void runSyncWithInterval(final long interval, final TimeUnit timeUnit, final Runnable runnable)
    {
        scheduler.createTaskBuilder().interval(interval, timeUnit).execute(runnable).submit(this.plugin);
    }

    public void runAsyncWithInterval(final long interval, final TimeUnit timeUnit, final Runnable runnable)
    {
        scheduler.createTaskBuilder().async().interval(interval, timeUnit).execute(runnable).submit(this.plugin);
    }

    public void runAsyncWithInterval(final long interval, final TimeUnit timeUnit, final Consumer<Task> consumer)
    {
        scheduler.createTaskBuilder().async().interval(interval, timeUnit).execute(consumer).submit(this.plugin);
    }

    public Scheduler getUnderlyingScheduler()
    {
        return this.scheduler;
    }

    public SpongeExecutorService getAsyncExecutor()
    {
        return asyncExecutor;
    }

    public SpongeExecutorService getSyncExecutor()
    {
        return syncExecutor;
    }
}
