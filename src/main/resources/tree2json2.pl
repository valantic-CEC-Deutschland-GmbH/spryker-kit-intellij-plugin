#! /usr/bin/perl

use File::Find;
use JSON;

use strict;
use warnings;

my $dirs={};
my $encoder = JSON->new->ascii->pretty;

find({wanted => \&process_dir, no_chdir => 1 }, ".");
print $encoder->encode($dirs);

sub process_dir {
    # return if !-d $File::Find::name;
    my $ref=\%$dirs;
    for(split(/\//, $File::Find::name)) {
        $ref->{$_} = {} if(!exists $ref->{$_});
        $ref = $ref->{$_};
    }
}
