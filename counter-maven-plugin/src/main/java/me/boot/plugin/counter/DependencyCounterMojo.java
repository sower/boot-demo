package me.boot.plugin.counter;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * DependencyCounterMojo
 *
 * @since 2024/03/16
 **/
@Mojo(name = "dependency-counter", defaultPhase = LifecyclePhase.COMPILE)
public class DependencyCounterMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    @Parameter(property = "scope")
    String scope;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        boolean isBlank = StringUtils.isBlank(scope);
        List<Dependency> dependencies = project.getDependencies();
        long numDependencies = dependencies.stream()
            .filter(dependency -> isBlank || scope.equals(dependency.getScope()))
            .count();
        getLog().info("Number of dependencies: " + numDependencies);
    }

}